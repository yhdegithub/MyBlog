public class KMP {
    public static void main(String[] args) {
        String target = "11010101110010101010"; String mode = "0010"; // 模式串// 主串
        char[] t = target.toCharArray();char[] m = mode.toCharArray();      
        System.out.println(matchProcess(t, m));
        } // KMP匹配字符串
        //计算部分匹配表，next[]值求法：一二位固定为0、1，其余位为部分匹配值+1.next[]值求法：一二位固定为0、1，其余位为部分匹配值+ //结构next[]数组
    public static int[] next(char[] t) {
        int[] next = new int[t.length];
        next[0] = -1;
        int i = 0;
        int j = -1;
        while (i < t.length - 1) {
            if (j == -1 || t[i] == t[j]) {
                i++;
                j++;
                if (t[i] != t[j]) {
                    next[i] = j;
                } else {
                    next[i] = next[j];
                }
            } else {
                j = next[j];
            }
        }
        return next;
    }
      //字符串匹配
        public static int matchProcess ( char[] s, char[] t){
            int[] next = getNext(t);
            int i = 0;
            int j = 0;
            while (i <= s.length - 1 && j <= t.length - 1) {
                if (j == -1 || s[i] == t[j]) {
                    i++;
                    j++;
                } else {
                    j =  next[j];
                }
            }
            if (j < t.length) {
                return -1;
            } else return i - t.length; // 返回模式串在主串中的头下标
        }
    }
