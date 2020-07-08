package com.tomcat.dubug.tomcat_dubug.Controller;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author linsong.chen
 * @date 2020-07-03 17:44
 * 模拟两百个服务器，既两百个线程
 */
//@Component
public class ThreadCallBack implements ApplicationContextAware {

    Map map = new ConcurrentHashMap();

    RestTemplate restTemplate = new RestTemplate();


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        for (int i = 0;i < 200;i++){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Thread thread = new Thread(){
                public void run(){
                    while (true){
                        restTemplate.getForObject("http://www.baidu.com",String.class);
                        System.out.println("threadId:"+Thread.currentThread());
                        try {
                            Thread.sleep(50000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            };

            thread.setDaemon(true);
            thread.start();
            map.put(i,thread);
        }

    }
}
