import java.util.Scanner;
public class Quick
{
    private static boolean less(Comparable v,Comparable w)  {  return v.compareTo(w)<=0; }   //是否小于

    private static void show(Comparable a[])    { for(int i=0;i<a.length;i++) System.out.print(a[i]+" ");       }                              //打印数组

    public static boolean isSorted(Comparable a[])  {  for(int i=1;i<a.length;i++)  if(less(a[i],a[i-1]))  return false;  return true;                }   //判定是否有序

    public static void sort(Comparable a[])   {                sort(a,0,a.length-1);  }                    //  排序

    private static void exch ( Comparable a[],int i,int j )   {   Comparable t=a[i]; a[i]=a[j];  a[j] = t ;                 }      //交换

     //快速排序的精髓
    private static void sort(Comparable a[],int low,int high)
    {

       if(low>=high)   return ;
        int pos=partition(a,low,high);                //j位置已经放好了
        sort(a,low,pos-1);                  //a[j]左右各自排好
        sort(a,pos+1,high);
    }
    //快速排序的精髓
     private static int partition(Comparable a[],int low,int high)
    {    //把数组切分成a[low...i-1],a[i],a[i+1...high]
        
        Comparable p=a[low];                  //划分基准
        while(low<high)
        {   
	        while( low<high&& less(p,a[high])  )    high--;       a[low]=a[high];
            while( low<high&& less(a[low],p)    )   low++;      a[high]=a[low];
         
               	//	exch(a,low,high);	

        }
      a[high]=p;
      return  high;                //返回下次排序的分界点
    }

    //快速排序的精髓


    public static void main(String args[])
    {
        System.out.println("请输入数据，并且空格隔开：");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine().toString();
        String a[] = input.split(" ");


        System.out.println("j检查结果是否为有序的："+isSorted(a));
        System.out.println("排序之后为：");

        sort(a);

        show(a);
    }

}





