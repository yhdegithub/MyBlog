/**
 * 判断str2是否在str1中，是则返回起始字符索引在str1中的索引，否则返回-1
 */
public class KMP {

    public static int getIndexOf(String str1, String str2) {
        if (str1 == null || str2 == null || str2.length() < 1 || str1.length() < str2.length()) {
            return -1;
        }
        int i = 0;
        int j = 0;
        int[] nexts = getNext(str2);
        while (i < str1.length() && j < str2.length()) {
            if (str1.charAt(i) == str2.charAt(j)) {
                i++;
                j++;
            } else if (j == 0) {
                i++;
            } else {
                j = nexts[j];
            }
        }
        return j == str2.length() ? i - j : -1;
    }

    public static int[] getNext(String str2) {
        int len = str2.length();
        if (len == 1) {
            return new int[]{-1};
        }

        int[] nexts = new int[len];
        nexts[0] = -1;
        nexts[1] = 0;

        int cn = 0;
        for (int i = 2; i < len; i++) {
            if (str2.charAt(i - 1) == cn) {
                nexts[i++] = ++cn;
            } else if (cn > 0) {
                cn = nexts[cn];
            } else {
                nexts[i++] = 0;
            }
        }
        return nexts;
    }

    public static void main(String[] args) {
        String target = "aca abacad"; String mode = "abacad"; // 模式串// 主串
        System.out.println(getIndexOf(target,mode));
        System.out.println(target.indexOf(mode));
    }
}


参考博客
https://blog.csdn.net/chanmufeng/article/details/83868088 ，之前写的是错误的 ，以后看笔记注意到
