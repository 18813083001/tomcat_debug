package com.tomcat.dubug.tomcat_dubug;

import org.apache.tomcat.util.threads.LimitLatch;
import org.junit.Test;

/**
 * @author linsong.chen
 * @date 2020-06-09 21:38
 */
public class LimitSynch {

    LimitLatch limitLatch = new LimitLatch(1);

    @Test
    public void test(){

        //生成队列
        LimitThread thread1 = new LimitThread();
        thread1.start();
        LimitThread thread2 = new LimitThread();
        thread2.start();
        LimitThread thread3 = new LimitThread();
        thread3.start();
        LimitThread thread4 = new LimitThread();
        thread4.start();

        //释放锁
        System.out.println("all:"+limitLatch.getQueuedThreads().size());
        thread1.countDown();
        System.out.println("count1:"+limitLatch.getQueuedThreads().size());
        thread2.countDown();
        System.out.println("count2:"+limitLatch.getQueuedThreads().size());
        thread3.countDown();
        System.out.println("count3:"+limitLatch.getQueuedThreads().size());
    }


    class LimitThread extends Thread{

        @Override
        public void run() {
            try {
                limitLatch.countUpOrAwait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void countDown(){
            limitLatch.countDown();
        }
    }

}
