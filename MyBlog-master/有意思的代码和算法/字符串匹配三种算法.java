/**
 * 字符串匹配算法 ： 暴力匹配   KMP      BM
 */

import java.util.*;

public class StringMatch {
    //法一 ： 暴力匹配
    public int BF(String s, String t) {
        if (t == null || t.length() == 0 || s == null || s.length() < t.length())
            return -1;
        int m = s.length(), n = t.length();
        for (int i = 0; i <= m - n; i++) {
            int j = 0;
            for (j = 0; j < n; j++) {
                if (s.charAt(i + j) == t.charAt(j))
                    continue;
                else
                    break;
            }
            if (j == n) return i;
        }
        return -1;
    }

    /*
     * KMP 算法  : 第一步求next[]数组 ，第二部就是比较
     */
    public int KMP(String s, String t) {
        if (s == null || t == null || s.length() < t.length() || t.length() == 0)
            return -1;
        int m = s.length(), n = t.length();
        int next[] = getNext(t.toCharArray());
        int i = 0, j = 0;
        while (i < m && j < n) {
            if (j == -1 || s.charAt(i) == t.charAt(j)) {
                i++;
                j++;
            } else
                j = next[j];
        }
        if (j == n)
            return i - j;
        return -1;
    }

    public int[] getNext(char t[]) {
        int n = t.length;
        int next[] = new int[n];
        int j = 1;
        int k = -1;
        next[0] = -1;
        while (j < n-1) {
            if (k == -1 || t[j] == t[k]) {
                j++;
                k++;
                next[j] = k;
            } else
                k = next[k];
        }
        return next;
    }

    /**
     * Booyer - Moore算法 ： 首先用一个 right[256] 记载 t中每个字符串出现的最右边的位置,默认都为 -1
     * 从最后一个字符串开始匹配,成功则 i 往前移动
     * 否则 每次可以跳跃 j - right [t.charAt(i+j )] 步数
     */
    public int BM(String s, String t) {
        if (s == null || t == null || s.length() < t.length() || t.length() == 0)
            return -1;
        int m = s.length();
        int n = t.length();
        //获取最右边字符出现位置
        int right[] = new int[256];
        Arrays.fill(right, -1);
        for (int i = 0; i < n; i++)
            right[t.charAt(i)] = i;
        int skip = 0;
        for (int i = 0; i <= n - m; i++) {
            skip = 0;
            for (int j = n - 1; j >= 0; j--) {
                if (t.charAt(j) != s.charAt(i + j)) {
                    skip = j - right[s.charAt(i + j)];
                    if (skip <= 0) skip = 1;
                    break;
                }
                if (skip == 0) return i;               //找到字符串了
            }
        }

        return -1;
    }
  //测试函数
  public static void main(String args[]) {
        String s = "BBC ABCDAB ABCDABCDABDE";
        String t = "ABCDABD";
        System.out.println("库函数结果，采用暴力法 ：" + s.indexOf(t));
        StringMatch sm = new StringMatch();
        System.out.println(" BF ： " + sm.BF(s, t));
        System.out.println(" KMP :  " + sm.KMP(s, t));
        System.out.println(" BM : " + sm.BF(s,t));
    }

}
