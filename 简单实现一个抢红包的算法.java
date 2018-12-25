package Test;

import java.util.Random;

/**
 *  设计一个抢红包的算法 ，维护一个剩余总金额 leftMoney ， 和剩余红包个数 leftCount 。
 *   1) leftCount = 0   提醒抢完了
 *   2) leftCount==1    返回剩下的总金额
 *   3）计算红包平均值 ， 生成随机数 rand ，如果 rand <0.001 ,则取0.001
 *    设定红包最大值为平均值的两倍
**/
 public class RandomRedPacket {
   private int leftMoney;
   private int leftCount;
   private Random rnd;
   public  RandomRedPacket(int total ,int num){
       this.leftMoney =total;
       this.leftCount = num;
       this.rnd = new Random();
   }
   public synchronized  int nextMoney(){
       if(this.leftCount<=0){
           throw new IllegalStateException("你来晚了 ，红包已抢完");
       }else if(this.leftCount==1){
           return this.leftMoney;
       }
       double max = this.leftMoney * 1.0 / this.leftCount * 2;
       int money = (int) (rnd.nextDouble()* max);
       this.leftMoney -= money;
       this.leftCount--;
       return money;
   }
   //保证先来的不一定抢到最大的  ， 不过后来的也不一定 ，这要看前面抢的金额，剩下的越多就有可能越大
   public static void main(String args[]){
       RandomRedPacket rrp = new RandomRedPacket((int)1000,(int)10);
       for(int i=1;i<=10;i++){
           System.out.println(" 第 " + i + " 个人抢到 ： " +  rrp.nextMoney() + " 元红包");
       }
   }
 }
