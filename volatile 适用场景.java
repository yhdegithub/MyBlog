//用 volatile实现简易的读写锁；
public class Example{
    private volatile long count;
    public long value(){
        return count;
    }
    public void increment(){
        synchronized(this){
            count++;
        }
    }
}

// volatile 作为状态标志 。 在某个场景中 ，一个状态变量仅由一个线程设置 ，其他线程会读取此状态作为计算的判定依据



// 在保证可见性上的保障。


// volatile 变量替代锁 ， 比如volatile long/double , 再比如 一个volatile引用 ， 其可保证对引用本身的操作起作用 ， 但是引用的值则不能保证了 