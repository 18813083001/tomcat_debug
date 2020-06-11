package com.tomcat.dubug.tomcat_dubug;

import com.tomcat.dubug.tomcat_dubug.autoconfig.ClassPathOnConditionConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.CountDownLatch;
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
	}

}
