package com.tomcat.dubug.tomcat_dubug;

import org.junit.Test;

public class BlockTest {

    class BlockedTest{

        public synchronized void notifyMethod(){
            //通知其他线程重新进入同步块
            this.notify();
            try {
                System.out.println("notify线程已经执行，未释放锁,等待30秒，观察wait线程状态");
                Thread.sleep(30000);
                System.out.println("notify线程释放锁，观察wait线程状态");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public synchronized void waitMethod(){
            try {
                System.out.println(Thread.currentThread().getName()+"线程开始等待");
                this.wait();
                System.out.println(Thread.currentThread().getName()+"线程重新获取锁，等待结束");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testBlocked() throws InterruptedException {

        BlockedTest blockedTest = new BlockedTest();


        Thread waitThread = new Thread(new Runnable() {
            @Override
            public void run() {
                blockedTest.waitMethod();
            }
        });
        waitThread.setName("wait");

        Thread.sleep(60000);//等待60秒，1.确保等待线程已经开始执行,2.打开jvisualvm，缓冲一点时间

        Thread notifyThread = new Thread(new Runnable() {
            @Override
            public void run() {
                blockedTest.notifyMethod();
            }
        });
        notifyThread.setName("notify");




    }
}
