package com.tomcat.dubug.tomcat_dubug.Controller;

import org.apache.tomcat.util.threads.LimitLatch;


/**
 * @author linsong.chen
 * @date 2020-06-09 21:38
 */
public class LimitSynch {

    static LimitLatch limitLatch = new LimitLatch(1);

    public static void main(String[] args) {

        //生成队列
        LimitThread thread1 = new LimitThread(1);
        thread1.start();
        LimitThread thread2 = new LimitThread(2);
        thread2.start();
        LimitThread thread3 = new LimitThread(3);
        thread3.start();
    }



    static class LimitThread extends Thread{


        int number;
        public LimitThread(int number) {
            super("thread-"+number);
            this.number = number;
        }

        @Override
        public void run() {
            try {
                limitLatch.countUpOrAwait();
                System.out.println("执行任务"+number);
                limitLatch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void countDown(){
            limitLatch.countDown();
        }
    }
}
