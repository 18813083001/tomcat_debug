package com.tomcat.dubug.tomcat_dubug;

import org.apache.tomcat.util.http.fileupload.MultipartStream;
import org.junit.Test;

import java.io.IOException;

/**
 * @author linsong.chen
 * @date 2020-07-18 17:51
 */
public class Kmp {

    @Test
    public void kmp() throws IOException {
        byte[] boundary = {'A','B','C','D','A','B'};
        MultipartStream multipartStream = new MultipartStream(null,boundary,1024,null);

        multipartStream.skipPreamble();
        System.out.println();

    }
}
