package com.tomcat.dubug.tomcat_dubug.tomcat;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.Executor;

@Component
public class RegistryTomcatTaskCount implements ApplicationContextAware {

    ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        if (applicationContext instanceof ServletWebServerApplicationContext){
//            if (((ServletWebServerApplicationContext) applicationContext).getWebServer() instanceof TomcatWebServer){
//                TomcatWebServer webServer = (TomcatWebServer) ((ServletWebServerApplicationContext) applicationContext).getWebServer();
//                Connector[] connectors = webServer.getTomcat().getService().findConnectors();
//                if (connectors != null){
//                    for (Connector connector : connectors){
//                       if ( connector.getProtocolHandler()!=null &&  connector.getProtocolHandler() instanceof Http11NioProtocol){
//                           Executor executor = connector.getProtocolHandler().getExecutor();
//                           if (executor instanceof ThreadPoolExecutor){
//                               printTomcatTaskCount((ThreadPoolExecutor) executor);
////                    System.out.println(((ThreadPoolExecutor) executor).getTaskCount());
//                           }
//                       }
//                    }
//                }
//            }
        }
    }




    public void printTomcatTaskCount(ThreadPoolExecutor executor){
        Thread printTaskCount = new Thread( new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        System.err.println(new Date() +" 当前Tomcat任务队列大小："+((ThreadPoolExecutor) executor).getQueue().size());
                        Thread.sleep(1000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        printTaskCount.setName("printTaskCount");
        printTaskCount.setDaemon(true);
        printTaskCount.start();


    }


    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        System.out.println("hello world, I have just started up");

        if (applicationContext instanceof ServletWebServerApplicationContext){
            if (((ServletWebServerApplicationContext) applicationContext).getWebServer() instanceof TomcatWebServer){
                TomcatWebServer webServer = (TomcatWebServer) ((ServletWebServerApplicationContext) applicationContext).getWebServer();
                Connector[] connectors = webServer.getTomcat().getService().findConnectors();
                if (connectors != null){
                    for (Connector connector : connectors){
                       if ( connector.getProtocolHandler()!=null &&  connector.getProtocolHandler() instanceof Http11NioProtocol){
                           Executor executor = connector.getProtocolHandler().getExecutor();
                           if (executor instanceof ThreadPoolExecutor){
                               printTomcatTaskCount((ThreadPoolExecutor) executor);
//                    System.out.println(((ThreadPoolExecutor) executor).getTaskCount());
                           }
                       }
                    }
                }
            }
        }
    }
}
