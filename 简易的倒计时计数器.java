import java.util.*;
import java.lang.Thread;
public class SimpleTimer {
    private static int count;
    public static void main(String args[]){
        count = args.length>=1 ? Integer.valueOf(args[0]) :10;  //赋值计数器初始值
        int remaining;
        final int num = Runtime.getRuntime().availableProcessors();
        System.out.println("处理器个数 ： " + num);
        while(true){
            remaining = countDown();
            if(0==remaining){
                break;
            }else{
                System.out.println("Remaining " + count + " seconds");
            }
            try{
                Thread.sleep(1000);
            }catch (Exception e){
                e.printStackTrace();
            }
            System.out.println("Done.");
        }
    }
    //减一
    private static int countDown(){
        return count--;

    }
}
