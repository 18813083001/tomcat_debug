package com.tomcat.dubug.tomcat_dubug.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author linsong.chen
 * @date 2020-07-16 15:02
 */
@RestController
@RequestMapping(value = "jmx/cpu")
public class CpuLoadController {

    Queue<BusyThread> threadQueue = new ConcurrentLinkedQueue<BusyThread>();

    /**
     * @param  numCore  cpu核数
     * @param  numThreadsPerCore  每个核的线程数
     * @param  load     希望达到的cpu利用率
     * @param  numCore  持续时间，毫秒
     * */
    @RequestMapping(value = "increase")
    public String increaseCpuLoad(int numCore,int numThreadsPerCore,double load,long duration){
        if (numCore == 0)
            numCore = 4;
        if (numThreadsPerCore == 0)
            numThreadsPerCore = 2;
        if (load <= 0)
            load = 0.7;
        if (duration == 0)
            duration = 100000;

        for (int thread = 0; thread < numCore * numThreadsPerCore; thread++) {
            BusyThread busyThread = new BusyThread("Thread" + thread, load, duration);
            busyThread.start();
            threadQueue.add(busyThread);
        }
        return "ok";
    }

    @RequestMapping(value = "stop")
    public String stopCupLoad(){
        for (BusyThread thread:threadQueue){
            thread.setDuration(0);
        }
        threadQueue.clear();
        return "ok";
    }



    /**OutOfMemoryError
     * Thread that actually generates the given load
     * @author Sriram
     */
    private static class BusyThread extends Thread {
        private double load;
        private long duration;

        /**
         * Constructor which creates the thread
         * @param name Name of this thread
         * @param load Load % that this thread should generate
         * @param duration Duration that this thread should generate the load for
         */
        public BusyThread(String name, double load, long duration) {
            super(name);
            this.load = load;
            this.duration = duration;
        }

        /**
         * Generates the load when run
         */
        @Override
        public void run() {
            long startTime = System.currentTimeMillis();

            // Loop for the given duration
            while (System.currentTimeMillis() - startTime < duration) {
                // Every 100ms, sleep for the percentage of unladen time
                if (System.currentTimeMillis() % 100 == 0) {
                    try {
                        Thread.sleep((long) Math.floor((1 - load) * 100));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        public void setDuration(long duration) {
            this.duration = duration;
        }
    }
}
