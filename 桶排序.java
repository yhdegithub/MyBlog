/**
 *                                                       桶排序
 以前的基数排序，都是桶排序的特殊情况 ，也就是每个桶的大小都是固定位1的情况 ， 所以基表适合于数据非常集中的时候 ，在统计一个群体的年龄 ， 考试分数， 工资分布排序还比较适合，但是如果数据差距比较大的就不好统计了
 桶排序不稳定，在理想情况下可以到达O(n)的时间复杂度 ，比快速排序还快，但是公认它空间开销很大很大，最适合于数据均匀分布的情况
 原理 ： 把数组num[] 划分成（max  - min）/n +1个桶，然后遍历一次把每个数据放到合适的桶里面 ； 紧接着可以对每个桶排序 ， 最后再编历一次所有的桶就可以得到最后的排序结果了
 过程： （1）找到数组的最大值max ,最小值 min
        （2）用动态数组ArrayList<ArrayList<>()>构造若干个桶 ， 一般是（max  - min）/n +1
        （3） 遍历原数组num,把num[i]放到某一号码的桶
        （4）每个桶自己排序
        （5）从小到大输出每个桶的数据
 * 
 * 
 * */
import java.util.*;
public class  Solution{
  public void bucketSort(int num[]){
      int max = num[0] ,min = num[0];
      //找到最大，最小值
      int n = num.length;
      for(int i=0;i<n;i++){
          max = Math.max(max,num[i]);
          min = Math.min(min,num[i]);
      }
      //设置几个桶,一般按套路
      int cnt = (max - min) / n  + 1;
      ArrayList<ArrayList<Integer>> buck = new ArrayList<>(cnt);
      for(int i=0;i<cnt;i++)
          buck.add(new ArrayList<>());
     //放置每个数据到适合的桶
      for(int i=0;i<n;i++){
          int k = (num[i] - min) / n ; //放置的桶的编号 ，要求是 < cnt;
          buck.get(k).add(num[i]);
      }
      //每个桶排序
      for(int i=0;i<cnt;i++)
          Collections.sort(buck.get(i));
      //打印每个桶
      System.out.println(buck.toString());
  }
    public static void main(String args[]){
        int num[] = new int[]{19,27,3,7,17,32,6,9,12};
        new Solution().bucketSort(num);
    }
}