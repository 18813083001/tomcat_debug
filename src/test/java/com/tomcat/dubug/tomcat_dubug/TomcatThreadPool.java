package com.tomcat.dubug.tomcat_dubug;

import org.apache.tomcat.util.threads.TaskQueue;
import org.apache.tomcat.util.threads.TaskThreadFactory;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;

import java.util.concurrent.TimeUnit;

public class TomcatThreadPool {

    public static void main(String[] args) {
        TaskQueue taskqueue = new TaskQueue();
        TaskThreadFactory tf = new TaskThreadFactory("test" + "-exec-", true, 5);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 200, 60, TimeUnit.SECONDS,taskqueue, tf);
        taskqueue.setParent( (ThreadPoolExecutor) executor);

        for (int i = 0;i < 400;i++){
            final int j = i;
            executor.execute(new Runnable() {
                public void run() {
                    System.out.println("第"+j+"个任务执行,然后睡两秒");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        executor.shutdown();
        while (!executor.isTerminated()){
            Thread.yield();
            System.out.println("activity count:"+executor.getActiveCount());
        }
        System.out.println("任务执行完毕");
    }
}
