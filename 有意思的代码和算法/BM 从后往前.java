//成功运行
import java.util.Scanner;
import java.io.*;
public class  BM
{
    public static int right[] = new int[256];
    public static void main(String args[])
    {       FileInputStream fis;
            try{
            fis = new FileInputStream("D:\\a.txt");
            Scanner sc =new Scanner(fis,"UTF-8");
            String txt =sc.nextLine().toString();
            String pat = sc.nextLine().toString();

            for(int i=0;i<256;i++)  right[i]=-1;            //**********************
            for(int j=0;j<pat.length();j++)   right[ pat.charAt(j) ]=j;		  //设置right[]为字符在pat中最右边出现的位置，默认-1；
            System.out.println();
            System.out.println("txt："+txt+ "    pat:"+pat);
            //int result = boyermoore(txt,pat);
            System.out.println(boyermoore(txt,pat));
            }catch (FileNotFoundException e)  {   e.printStackTrace();    }
    }
    public static int boyermoore(String txt,String pat)
    {
        int n=txt.length(),m=pat.length();
        int skip;
		int right[] =new int[256];
		Array.fill(right,-1);
		for(int i=0;i<m;i++)
			right[pat.charAt(i)]=i;
        for(int i=0;i<=n-m;i+=skip)
        {
            skip=0;
            for(int j=m-1;j>=0;j--)
                if(txt.charAt(i+j) != pat.charAt(j) )
                {
                    skip = j - right[txt.charAt(j+i)];
                    if (skip<=0) skip=1;                 //防止得到skip=0甚至负数，保证skip>=1。向前移动
                    System.out.println("移动"+skip);
                    break;               //不匹配，则i后移skip步

                }
            if(skip==0)      {    System.out.println("-成功-");   return i ; }  //匹配完成
        }

        return -1;
    }
}
