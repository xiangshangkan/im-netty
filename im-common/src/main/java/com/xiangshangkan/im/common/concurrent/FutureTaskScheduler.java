package com.xiangshangkan.im.common.concurrent;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description: ${description}
 * @Author: Zohar
 * @Date: 2020/7/11 16:14
 * @Version: 1.0
 */
@Slf4j
public class FutureTaskScheduler extends Thread{
    //任务队列
    private ConcurrentLinkedQueue<ExecuteTask> executeTaskQueue = new ConcurrentLinkedQueue<>();
    //线程休眠时间
    private long sleepTime = 200;
    //线程池
    private ExecutorService pool = Executors.newFixedThreadPool(10);

    private static FutureTaskScheduler inst = new FutureTaskScheduler();

    private FutureTaskScheduler(){this.start();}

    /**
     * 添加任务
     * @param executeTask
     */
    public static void add(ExecuteTask executeTask) {
        inst.executeTaskQueue.add(executeTask);
    }

    @Override
    public void run() {
        while(true) {
            //处理任务
            handleTask();
            threadSleep(sleepTime);
        }
    }

    private void threadSleep(long time) {
        try {
            sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理任务队列，检查其中是否有任务
     */
    private void handleTask(){
        try {
            ExecuteTask executeTask;
            while(executeTaskQueue.peek() != null) {
                executeTask = executeTaskQueue.poll();
                handleTask(executeTask);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 执行任务操作
     * @param executeTask
     */
    private void handleTask(ExecuteTask executeTask) {
        pool.execute(new ExecuteRunnable(executeTask));
    }


    class ExecuteRunnable implements Runnable {
        ExecuteTask executeTask;

        ExecuteRunnable(ExecuteTask executeTask){
            this.executeTask = executeTask;
        }

        @Override
        public void run(){
            executeTask.execute();
        }
    }

}
