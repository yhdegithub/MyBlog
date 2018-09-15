//暴力字符串匹配
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class Fileraad
{
    public static int search(String txt,String pat)
    {
        int i,j;
        int n=txt.length();
        int m=pat.length();
        System.out.println( n +"  "+m);

        for(i=0,j=0;i<n && j< m;i++)
        {
            if(pat.charAt(j)==txt.charAt(i))  {  j++; ;}
            else
            {
                i=i-j;
                j=0;
            }
   }
        if(j==m)	return  i-m;
        else return -1;
    }
   public static void main(String args[])
    {
       // String txt,pat;

        try{
            FileInputStream fis =new FileInputStream("D:\\a.txt");
            Scanner sc =new Scanner (fis,"UTF-8");
            String txt=sc.nextLine().toString();
            String pat=sc.nextLine().toString();
            System.out.println(txt+"\n"+pat);
            System.out.println(search(txt,pat));
        }   catch(FileNotFoundException e) {e.printStackTrace();}

    }
}