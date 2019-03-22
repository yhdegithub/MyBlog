/*动态规划
   dp[i][j]表示s的前i个字符和p的前j个字符是否匹配
https://www.cnblogs.com/sunshine-2015/p/7277315.html

*/
import java.util.*;
public class Solution {
  public boolean isMatch (String s, String p) {
       //对p长度分类讨论 0,1,2（p[1]与*的两种关系，p[1]==*又包含p[0]==s[0] ,p[0]!=s[0]俩种）
        if(s==null)   return p==null;
        if(p.length()==0)         //p长度为0
            return s.length()==0;
        if(p.length()==1){      //p长度为1
            if(s.length()==1 && check(s.charAt(0),p.charAt(0)))
                return true;
               return false;
        }
        //p长度为2,有p[1]!=* , p[1]=='*',后者又有2种
      if(p.charAt(1)!='*'){           // p[1] != ‘*’
            if(s.length()==0)
                return false;
          return check(s.charAt(0),p.charAt(0)) ? isMatch(s.substring(1),p.substring(1)) : false;
      }
     // p[1]=='*' ， 当前字符p[0]与s[0]匹配
       while(s.length()!=0 && check(s.charAt(0),p.charAt(0))) {
            if(isMatch(s,p.substring(2)) )    //后移2个 ab  a*b => b , b
                return true;
            s = s.substring(1);      //s后移1个，和while一起构成 a*  =>   aaaa....
       }
        // p[1]=='*' ， 当前字符p[0]与s[0]不匹配
        return isMatch(s,p.substring(2));
    }

    public boolean check(char a , char b){
        return a==b || a=='.'|| b=='.';
    }
}









