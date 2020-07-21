package com.tomcat.dubug.tomcat_dubug;

// JAVA program for implementation of KMP pattern
// searching algorithm

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class KMP_String_Matching {
    List<Integer> KMPSearch(String pat, String txt)
    {
        List<Integer> list = new ArrayList<>();
        int M = pat.length();
        int N = txt.length();

        // create lps[] that will hold the longest
        // prefix suffix values for pattern
        int lps[] = new int[M];
        int j = 0; // index for pat[]

        // Preprocess the pattern (calculate lps[]
        // array)
        computeLPSArray(pat, M, lps);
        long start = System.currentTimeMillis();

        int i = 0; // index for txt[]
        while (i < N) {
            if (pat.charAt(j) == txt.charAt(i)) {
                j++;
                i++;
            }
            if (j == M) {
//                System.out.println("Found pattern "
//                        + "at index " + (i - j));
                list.add(i - j);
                j = lps[j - 1];

            }

            // mismatch after j matches
            else if (i < N && pat.charAt(j) != txt.charAt(i)) {
                // Do not match lps[0..lps[j-1]] characters,
                // they will match anyway
                /**
                 *    i
                 * 123456
                 * 123567
                 *    j
                 *    123456
                 *
                 *        i
                 *  12312345
                 *  12312356
                 *        j
                 *     12312356
                 *
                 * */
                if (j != 0)
                    j = lps[j - 1];
                else
                    i = i + 1;
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("KMPSearch耗时："+(end-start));
        return list;
    }

    /**
     * 说明1：len
     *
     * @param pat 模式
     * @param M pat的长度
     * @param lps 存储最长前缀
     * */
    public void computeLPSArray(String pat, int M, int lps[])
    {
        // length of the previous longest prefix suffix
        /*
        * len表示前面字符串的最长前缀后缀的长度，比如字符串12341235，当前字符是5，那么len就表示5前面的字符串1234123的最长前缀后缀长度
        *
        * ken为什么可以做为下标使用？len表示前面字符串的最长匹配长度（比如前面字符串1234123，最长匹配字符串是123，即len=3），
        * 所以pat.charAt(len)就是最长匹配字符串的下一个字符（即pat.charAt(i)=4），当i=7，pat.charAt(i)=5时，即查找字符串12341235的最长匹配长度，
        * 原理是：如果5前面的子串1234123有最长匹配字符，如123，那么原则上是可以先匹配一下最长匹配后缀（如123）与当前字符（比如5）拼起来是1235和最长
        * 匹配前缀（也是123）与后一个字符（比如4）拼起来是1234看看是否相等，反正最长前缀和后缀一定是相等的，实际上和只比较4和5是否相等就可以了，如果
        * 相等，则最长匹配长度只需要再+1就可以了（想想？是不是这样），如果不相等，那字符串12341235的最长匹配长度不可能超过5前面的字符串1234123的最长匹配长
        * 度了，因为1234123的最长匹配长度字符串是123，最长前缀也是123，5既然匹配不了123后面的字符，那只能尝试匹配123前面的字符（想想？是不是这样，毕竟最长
        * 匹配字符是需要从字符串的开头开始，后面连续匹配的才行，123后面的匹配不了，就只能考虑123前面的字符），如果想要匹配123前面的字符，那可以先看看字符串
        * 123本身是否有最长匹配的前缀后缀（其实这里又回到了比较5和前面子串1234123的最长前缀后缀的地方），如果123有最长前缀后缀，那字符串12341235中靠近5的
        * 123也有最长前缀后缀，把后缀和5拼起来再与123的最长前缀的和前缀的后一个字符拼起来做比较，如果相等则12341235的最长前缀后缀就是123的最长前缀后缀+1，
        * （说明1处的代码实现这个逻辑），如果不相等，再看看123的最长匹配前缀（和后缀相等，把他们又看着一个子串）的最长匹配前缀，并重复上述动作，直到出现某个
        * 子串的最长匹配前缀后缀长度是0，为0表示没有找到最长前缀后缀，没有找到意味着需要将当前字符与字符串的第一个字符进行比较（假如pat字符串=123412356，
        * 当与txt文本比较到6时发生不匹配，这时候是不是应该找12341235的最长前缀后缀，但是发现12341235没有最长前缀后缀，那是不是应该直接12341235字符串的第一个
        * 字符1移动到6的位置，也就是移动8个位置，这个8就是12341235的长度），说明2出代码实现这个逻辑，当len=0时，也就是没有找到最长前缀后缀时，当前字符
        * pat.charAt(i)与第一个字符pat.charAt(len)即pat.charAt(0)比较
        * */
        int len = 0;
        int i = 1;
        lps[0] = 0; // lps[0] is always 0

        // the loop calculates lps[i] for i = 1 to M-1
        while (i < M) {
            if (pat.charAt(i) == pat.charAt(len)) { // 说明1
                len++;
                lps[i] = len;
                i++;
            }
            else // (pat[i] != pat[len])
            {
                // This is tricky. Consider the example.
                // AAACAAAA and i = 7. The idea is similar
                // to search step.
                if (len != 0) {
                    len = lps[len - 1];

                    // Also, note that we do not increment
                    // i here
                }
                else // if (len == 0)
                {
                    lps[i] = len; //说明2
                    i++;
                }
            }
        }
    }

    // Driver program to test above function
    public static void main(String args[]) throws IOException {
//        String txt = "ABABDABACDABABCABAB";
//        String pat = "ABABCABAB";
//        String txt = "asd123451234512345123";
//        String pat = "12345123";
        String txt = getTxtFromFile();

        String pat = "org.apache.catalina.core.StandardHostValve.invoke";
        System.out.println(new KMP_String_Matching().KMPSearch2(pat, txt));

    }

    private static String getTxtFromFile() throws IOException {
        String filePath = "/Users/chenlinsong/Downloads/err1.log";

//        BufferedReader reader = null;
//        try {
//            reader = new BufferedReader(new FileReader(filePath));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        String         line = null;
//        StringBuilder  stringBuilder = new StringBuilder();
//        String         ls = System.getProperty("line.separator");
//
//        try {
//            while((line = reader.readLine()) != null) {
//                stringBuilder.append(line);
//                stringBuilder.append(ls);
//            }
//
//            return stringBuilder.toString();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                reader.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        byte[] encoded = Files.readAllBytes(Paths.get(filePath));
        return new String(encoded, "utf-8");
    }

    public  List<Integer> KMPSearch2(String pat,String txt){
        long start = System.currentTimeMillis();
        List<Integer> index = new ArrayList<>();

        int i = 0;
        int length = txt.length();

        while (i < length){
            int p = i;
            for (int j = 0; j < pat.length() && i < length;j++){
                if (pat.charAt(j) == txt.charAt(i)){
                    i++;
                }else {
                    break;
                }
            }

            if (((i-p))==pat.length()){
                index.add(p);

            }else  i = p + 1;
        }
        long end = System.currentTimeMillis();
        System.out.println("KMPSearch2耗时："+(end-start));
        return index;

    }

    public  List<Integer> KMPSearch3(String pat,String txt){
        List<Integer> index = new ArrayList<>();

        int M = pat.length();
        int N = txt.length();

        // create lps[] that will hold the longest
        // prefix suffix values for pattern
        int lps[] = new int[M];

        // Preprocess the pattern (calculate lps[]
        // array)
        computeLPSArray(pat, M, lps);
        long start = System.currentTimeMillis();

        int i = 0;
        int length = txt.length();

        while (i < length){
            int p = i;
            int n = 0;//记录pat中哪个字符不匹配了
            for (int j = 0; j < pat.length() && i < length;j++){
                if (pat.charAt(j) == txt.charAt(i)){
                    i++;//i可能是达到98，导致n = j+1;不会执行
                    if (i == N)
                        n = j+1;
                }else {
                    n = j+1;
                    break;
                }
            }

            if (((i-p))==pat.length()){
                index.add(p);

            }else  {
                int increment = n-lps[n-1];//增量
                i +=increment;//当前位置向后移动increment个位置
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("KMPSearch3耗时："+(end-start));
        return index;

    }
}
// This code has been contributed by Amit Khandelwal.