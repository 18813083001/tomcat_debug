package com.tomcat.dubug.tomcat_dubug;

import org.junit.Test;

import java.io.IOException;
import java.net.Socket;

/**
 * @author linsong.chen
 * @date 2020-07-12 22:36
 */
public class SocketTest {

    @Test
    public void httpRequest() throws IOException, InterruptedException {

        String message = "1234567=90ab\r\n";
        Socket socket = new Socket("localhost",8081);

        int n=5;
        for (int m = 0; m < n; m++){
            socket.setKeepAlive(true);

            int k = 1;
            String httpResponse = "GET /tomcat/debug HTTP/1.1\r\n" +
                    "Content-Length: "+14*k+"\r\n" +
                    "Host: "+"localhost"+"\r\n"+
                    "Content-Type: text/html\r\n" +
                    "\r\n" +
                    ""+message+"";

            System.out.println("==:"+httpResponse.getBytes().length);
            socket.getOutputStream().write(httpResponse.getBytes());
            int i= 0;
//        while (i<k-1){
//            i++;
//            Thread.sleep(1000);
//            System.out.println("发送");
//            socket.getOutputStream().write(message.getBytes());
//        }
            socket.getOutputStream().flush();

            byte[] b = new byte[160];
            Thread.sleep(1500);
            System.out.println("接收");
            socket.getInputStream().read(b);
            System.out.println(new String(b,"utf-8"));
            System.out.println("\r\n----");
        }

        socket.close();

    }
}
