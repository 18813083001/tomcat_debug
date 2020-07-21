package com.tomcat.dubug.tomcat_dubug;

import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author linsong.chen
 * @date 2020-07-20 22:14
 */
public class KmpInputStream {

    public static void main(String[] args) throws IOException {
        String pat = "org.apache.catalina.core.StandardHostValve.invoke";
        KmpInputStream kmpInputStream = new KmpInputStream();
        System.out.println(kmpInputStream.KMPSearch(pat));
    }


    List<Integer> KMPSearch(String pat) throws IOException {
        List<Integer> list = new ArrayList<>();
        int M = pat.length();


        // create lps[] that will hold the longest
        // prefix suffix values for pattern
        int lps[] = new int[M];


        // Preprocess the pattern (calculate lps[]
        // array)
        KMP_String_Matching matching = new KMP_String_Matching();
        matching.computeLPSArray(pat, M, lps);

        long start = System.currentTimeMillis();


        String filePath = "/Users/chenlinsong/Downloads/err1.log";
        InputStream inputStream = new FileInputStream(filePath);
        int boundarLength = pat.length()*2;

        byte[] bytes = new byte[pat.length()];

        ByteBuffer byteBuffer = ByteBuffer.allocate(boundarLength);
        int len = 0;

        int startIndex = 0;
        while ( (len = inputStream.read(bytes)) != -1){
            //还有空间可写
            if (byteBuffer.remaining() >= len){
                byteBuffer.put(bytes,0,len);
            } else {
                //byteBuffer空间不够写了，先写一部分
                int remain = byteBuffer.remaining();
                for (int k =0;k < remain;k++){
                    byteBuffer.put(bytes[k]);
                }

                //剩余len-remain个字节未处理
                //先存下来

                byte[] undeal = new byte[len-remain];

                int m = 0;
                for (int k = remain;k < len;k++ ){
                    undeal[m] = bytes[k];
                    m++;
                }

                List<Integer> list1 = action(pat,lps,boundarLength,byteBuffer,startIndex);
                list.addAll(list1);

                //计算byteBuffer处理了多少个字节
                int position = byteBuffer.position();
                int dealByte = boundarLength - position;
                startIndex = startIndex + dealByte;

                byteBuffer.put(undeal,0,undeal.length);

            }
        }


        long end = System.currentTimeMillis();
        System.out.println("KMPSearch耗时："+(end-start));
        return list;
    }

    public List<Integer> action(String pat, int lps[],int boundarLength,ByteBuffer byteBuffer,int startIndex) throws UnsupportedEncodingException {

        List<Integer> indexList = new ArrayList<>();

        List<Integer> list = new ArrayList<>();
        int M = pat.length();
        int j = 0;
        int i = 0;

        //读
        byteBuffer.flip();
        byte[] bytes = byteBuffer.array();
        byteBuffer.clear();


        String txt = new String(bytes,"utf-8");
        int N = txt.length();
        while (i < N) {
            if (pat.charAt(j) == txt.charAt(i)) {
                j++;
                i++;
            }
            if (j == M) {
//                System.out.println("Found pattern "
//                        + "at index " + (i - j));
                int index = i - j;
                list.add(index);
                indexList.add(index+startIndex);

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

        //没有匹配的字符，既前面、中间、后面都没匹配到，则删除前面pat长度的字符
        if (list.size() == 0){
            byte[] newb = new byte[boundarLength];
            for (int k = 0;k < pat.length();k++){
                newb[k] = bytes[pat.length()+k];
            }
            byteBuffer.put(newb,0,pat.length());
//            bytes = newb;
        }else if (list.size() == 1){
            //匹配到一次
            int index = list.get(0);
            //去掉已经匹配的字符，包含匹配字符前面的字符
            int newIndex = index+pat.length();
            int newByteLength = boundarLength - newIndex;
            byte[] newb = new byte[newByteLength];
            for (int k = index+pat.length(), m=0;k < boundarLength && m<newByteLength;k++,m++){
                newb[m] = bytes[k];
            }
//            bytes = newb;
            byteBuffer.put(newb,0,newb.length);
        }else {
            //匹配了两次
//            bytes = new byte[boundarLength];
        }
        return indexList;
    }


    public  List<Integer> action2(String pat, int lps[],int boundarLength,ByteBuffer byteBuffer,int startIndex) throws UnsupportedEncodingException {
        List<Integer> indexList = new ArrayList<>();

        List<Integer> list = new ArrayList<>();

        int i = 0;
        //读
        byteBuffer.flip();
        byte[] bytes = byteBuffer.array();
        byteBuffer.clear();


        String txt = new String(bytes,"utf-8");
        int N = txt.length();

        while (i < N){
            int p = i;
            for (int j = 0; j < pat.length() && i < N;j++){
                if (pat.charAt(j) == txt.charAt(i)){
                    i++;
                }else {
                    break;
                }
            }

            if (((i-p))==pat.length()){
                list.add(p);
                indexList.add(p+startIndex);

            }else  i = p + 1;
        }

        //没有匹配的字符，既前面、中间、后面都没匹配到，则删除前面pat长度的字符
        if (list.size() == 0){
            byte[] newb = new byte[boundarLength];
            for (int k = 0;k < pat.length();k++){
                newb[k] = bytes[pat.length()+k];
            }
            byteBuffer.put(newb,0,pat.length());
//            bytes = newb;
        }else if (list.size() == 1){
            //匹配到一次
            int index = list.get(0);
            //去掉已经匹配的字符，包含匹配字符前面的字符
            int newIndex = index+pat.length();
            int newByteLength = boundarLength - newIndex;
            byte[] newb = new byte[newByteLength];
            for (int k = index+pat.length(), m=0;k < boundarLength && m<newByteLength;k++,m++){
                newb[m] = bytes[k];
            }
//            bytes = newb;
            byteBuffer.put(newb,0,newb.length);
        }else {
            //匹配了两次
//            bytes = new byte[boundarLength];
        }

        return indexList;

    }


    public  List<Integer> action3(String pat, int lps[],int boundarLength,ByteBuffer byteBuffer,int startIndex) throws UnsupportedEncodingException {
        List<Integer> indexList = new ArrayList<>();

        List<Integer> list = new ArrayList<>();
        int i = 0;

        //读
        byteBuffer.flip();
        byte[] bytes = byteBuffer.array();
        byteBuffer.clear();


        String txt = new String(bytes,"utf-8");
        int N = txt.length();

        while (i < N){
            int p = i;
            int n = 0;//记录pat中哪个字符不匹配了
            for (int j = 0; j < pat.length() && i < N;j++){
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
                list.add(p);
                indexList.add(p+startIndex);

            }else  {
                if (n == 0)
                    System.out.println();
                int increment = n-lps[n-1];//增量
                i +=increment;//当前位置向后移动increment个位置
            }
        }

        //没有匹配的字符，既前面、中间、后面都没匹配到，则删除前面pat长度的字符
        if (list.size() == 0){
            byte[] newb = new byte[boundarLength];
            for (int k = 0;k < pat.length();k++){
                newb[k] = bytes[pat.length()+k];
            }
            byteBuffer.put(newb,0,pat.length());
//            bytes = newb;
        }else if (list.size() == 1){
            //匹配到一次
            int index = list.get(0);
            //去掉已经匹配的字符，包含匹配字符前面的字符
            int newIndex = index+pat.length();
            int newByteLength = boundarLength - newIndex;
            byte[] newb = new byte[newByteLength];
            for (int k = index+pat.length(), m=0;k < boundarLength && m<newByteLength;k++,m++){
                newb[m] = bytes[k];
            }
//            bytes = newb;
            byteBuffer.put(newb,0,newb.length);
        }else {
            //匹配了两次
//            bytes = new byte[boundarLength];
        }

        return indexList;

    }

}
