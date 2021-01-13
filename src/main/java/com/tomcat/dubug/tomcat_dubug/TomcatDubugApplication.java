package com.tomcat.dubug.tomcat_dubug;

import com.tomcat.dubug.tomcat_dubug.autoconfig.ClassPathOnConditionConfig;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class TomcatDubugApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(TomcatDubugApplication.class, args);
		ClassPathOnConditionConfig config = context.getBean(ClassPathOnConditionConfig.class);
		if (config != null){
			System.out.println("--:"+config.grtString());
		}else {
			System.out.println("+++:classpath is null");
		}

		System.out.println(Thread.currentThread().getContextClassLoader());
		System.out.println(Thread.currentThread().getContextClassLoader().getParent());

		System.out.println("+++");
		System.out.println(System.getProperty("java.class.path"));

		System.out.println("+++");

		System.out.println(System.getProperty("java.ext.dirs"));
		System.out.println("---");
	}

}
