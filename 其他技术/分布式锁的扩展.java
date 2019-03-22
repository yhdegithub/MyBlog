做项目，使用到了分布式锁，参考博客做了一些实现，但是总觉得不全面，看完了zookeeper终于可以写的出来一点东西了。目前分布式锁主要有两种实现方式 ： 基于redis实现的 ， 基于zookeeper ,还有很少用到的基于数据库乐观锁的 。 一个良好的分布式锁，必须同一时刻只能最多有一个用户使用 ，用户使用完之后释放锁，释放的也是自己的锁，不是其他人的（主要是因为redis设置的超时时间的问题）



1）使用Redis实现的锁
最早开始 ，我在项目中是使用的setnx(lock , 用户的ip) ,  expire(ip  , expireTime) ,后来才看到网上有解释 ，这两个操作之间不是原子操作 ，可能在第一步成功，第二步就挂了 ，这就导致锁死了 ，其他用户也不能获得锁。
注意 ：也不能使用事务 ，因为redis的事务是不支持回滚的，伪事务
于是参考到有这样一个语句 set(lock , 用户的ip , set_if_not_exist,expireTime)。这样的操作才是一个真正的原子操作。
参考代码如下：
获得锁 ：
public class RedisTool {

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    /**
     * 尝试获取分布式锁
     * @param jedis Redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public static boolean tryGetDistributedLock(Jedis jedis, String lockKey, String requestId, int expireTime) {

        String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);

        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;

    }

}

这个代码核心就在String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);这个操作取代了才是真正的原子操作 。 
使用这个带参数的set命令，需要redis一定的版本 ，我之前项目中的就是不能用，升到redis3.2就可以了。
解释各个参数含义 ：
  1）lockkey . 
   这个是做分布式锁的 ，在秒杀项目中这个可以用商品的名称，id
  2）requestId . 这个是操作是为了防止其误删别人的锁，只能是自己的ip时候才能删除自己的锁。
  解释下 ：加入用户A获得了锁 ，设置了expireTime ， 但是在删除expireTime 之前就过期了，lockkey不存在了 ，这是用户B又可以拿锁进来了 。当用户B在执行之时，用户A就执行完业务，要删除锁了，此时删除的就是用户B的锁！！！！
  这就是requestID存在的意义 ：只能删除自己的锁
  3）NX .
  这是设置分布式锁的功能。只能之前不存在，之后众多用户中由成功执行set了的那个进程才能
 成功拿锁。
 4）Expiretime. 
  设置过期时间，应该比实际执行业务稍微长一点 。为了防止某个用户拿到锁后，给挂了，所以等到了一定时间，就必须自动放弃锁
  释放锁：
  public static void wrongReleaseLock2(Jedis jedis, String lockKey, String requestId) {

    // 判断加锁与解锁是不是同一个客户端
    if (requestId.equals(jedis.get(lockKey))) {
        // 若在此时，这把锁突然不是这个客户端的，则会误解锁
        jedis.del(lockKey);
    }

}
  
2）使用zookeeper

   Zookeeper就是一个提供分布式一致性服务的玩意儿 。 
  我们先在一个主机下创建一个永久节点ParentLock 
  获取锁
当第一个客户端想要获得锁时，需要在ParentLock这个节点下面创建一个临时顺序节点 Lock1。
之后，Client1查找ParentLock下面所有的临时顺序节点并排序，判断自己所创建的节点Lock1是不是顺序最靠前的一个。如果是第一个节点，则成功获得锁。
这时候，如果再有一个客户端 Client2 前来获取锁，则在ParentLock下载再创建一个临时顺序节点Lock2。
Client2查找ParentLock下面所有的临时顺序节点并排序，判断自己所创建的节点Lock2是不是顺序最靠前的一个，结果发现节点Lock2并不是最小的。
于是，Client2向排序仅比它靠前的节点Lock1注册Watcher，用于监听Lock1节点是否存在。这意味着Client2抢锁失败，进入了等待状态。
时候，如果又有一个客户端Client3前来获取锁，则在ParentLock下载再创建一个临时顺序节点Lock3。
Client3查找ParentLock下面所有的临时顺序节点并排序，判断自己所创建的节点Lock3是不是顺序最靠前的一个，结果同样发现节点Lock3并不是最小的。
于是，Client3向排序仅比它靠前的节点Lock2注册Watcher，用于监听Lock2节点是否存在。这意味着Client3同样抢锁失败，进入了等待状态。
这样一来，Client1得到了锁，Client2监听了Lock1，Client3监听了Lock2。这恰恰形成了一个等待队列，很像是Java当中ReentrantLock所依赖的AQS（AbstractQueuedSynchronizer）。

释放锁
1.任务完成，客户端显示释放。当任务完成时，Client1会显示调用删除节点Lock1的指令。
2.任务执行过程中，客户端崩溃
获得锁的Client1在任务执行过程中，如果Duang的一声崩溃，则会断开与Zookeeper服务端的链接。根据临时节点的特性，相关联的节点Lock1会随之自动删除。
由于Client2一直监听着Lock1的存在状态，当Lock1节点被删除，Client2会立刻收到通知。这时候Client2会再次查询ParentLock下面的所有节点，确认自己创建的节点Lock2是不是目前最小的节点。如果是最小，则Client2顺理成章获得了锁。

同理，如果Client2也因为任务完成或者节点崩溃而删除了节点Lock2，那么Cient3就会接到通知

   

3) 数据库乐观锁 。
    这个使用原理就是插入一个具有唯一性约束的数据行 ，只能有一个用户成功插入，其他的都失败，故成功插入的才能拿到锁 ； 释放锁可以直接用删除
	但是，在大量用户时，这种压力全都给后端数据库了 ，这种方式很少使用吧
    








































