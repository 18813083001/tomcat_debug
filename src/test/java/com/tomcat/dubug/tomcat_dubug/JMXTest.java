package com.tomcat.dubug.tomcat_dubug;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.util.Vector;

/**
 * @author linsong.chen
 * @date 2020-07-01 15:34
 */
public class JMXTest {

    @Test
    public void jmxTest(){
//
//        RestTemplate restTemplate = new RestTemplate();
//        String re = restTemplate.getForObject("http://localhost:8081/jmx/info?hostport=localhost:9999&beanname=java.lang:type=OperatingSystem&command=ProcessCpuTime",String.class);
//
//        System.out.println(re);

        RestTemplate restTemplate = new RestTemplate();

        for (int i =0;i < 200;i++){
            new Thread(){
                public void run(){

                    System.out.println("dd");
                    String re = restTemplate.getForObject("http://localhost:8081/jmx/info?hostport=localhost:9999&beanname=java.lang:type=OperatingSystem&command=ProcessCpuTime",String.class);
//
                    System.out.println(re);

                }
            }.start();
        }

    }
}
