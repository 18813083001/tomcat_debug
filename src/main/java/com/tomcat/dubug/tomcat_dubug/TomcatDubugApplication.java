package com.tomcat.dubug.tomcat_dubug;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class TomcatDubugApplication {

	public static void main(String[] args) {
		SpringApplication.run(TomcatDubugApplication.class, args);
		System.out.println(~100);
		CountDownLatch countDownLatch = new CountDownLatch(1);
		try {
			countDownLatch.await(1000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(countDownLatch.getCount());
	}

}
