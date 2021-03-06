package com.tomcat.dubug.tomcat_dubug;

import org.apache.tomcat.util.threads.LimitLatch;
import org.junit.Test;

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

    @Test
    public void test(){

        //生成队列
        LimitThread thread1 = new LimitThread(1);
        thread1.start();
        LimitThread thread2 = new LimitThread(2);
        thread2.start();
        LimitThread thread3 = new LimitThread(3);
        thread3.start();
//        LimitThread thread4 = new LimitThread();
//        thread4.start();

//        //释放锁
//        System.out.println("all:"+limitLatch.getQueuedThreads().size());
//        thread1.countDown();
//        System.out.println("count1:"+limitLatch.getQueuedThreads().size());
//        thread2.countDown();
//        System.out.println("count2:"+limitLatch.getQueuedThreads().size());
//        thread3.countDown();
//        System.out.println("count3:"+limitLatch.getQueuedThreads().size());
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
