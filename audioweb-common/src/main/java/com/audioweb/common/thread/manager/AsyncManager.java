package com.audioweb.common.thread.manager;

import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import com.audioweb.common.utils.Threads;
import com.audioweb.common.utils.spring.SpringUtils;

/**
 * 异步任务管理器
 * 
 * @author liuhulu
 */
public class AsyncManager
{
    /**
     * 操作延迟10毫秒
     */
    private final int OPERATE_DELAY_TIME = 10;

    /**
     * 异步操作任务调度线程池
     */
    private ScheduledExecutorService executor = SpringUtils.getBean("scheduledExecutorService");
    
    /**
     * Boss操作任务调度线程池
     */
    private ExecutorService bossExecutor = SpringUtils.getBean("BossServiceExecutor");
    
    /**
     * IO操作任务调度线程池
     */
    private ExecutorService ioExecutor = SpringUtils.getBean("IoServiceExecutor");
    
    /**
     * TCP操作任务调度线程池
     */
    private ExecutorService tcpExecutor = SpringUtils.getBean("TcpServiceExecutor");

    /**
     * 单例模式
     */
    private AsyncManager(){}

    private static AsyncManager me = new AsyncManager();

    public static AsyncManager me()
    {
        return me;
    }

    /**
     * 执行任务
     * 
     * @param task 任务
     */
    public void execute(TimerTask task)
    {
        executor.schedule(task, OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
    }
    /**
     * 执行任务
     * 
     * @param task 任务
     */
    public void execute(TimerTask task,int delay)
    {
    	executor.schedule(task, OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
    }

    /**
     * 停止任务线程池 
     */
    public void shutdown()
    {
        Threads.shutdownAndAwaitTermination(executor);
    }
    
    /**
     *获取组播线程池 
     */
    public ExecutorService getBossThreads() {
		return bossExecutor;
	}
    /**
     * io异步调用
     * 
     * @param thread 异步任务
     */
    public void ioExecute(Runnable thread) {
    	ioExecutor.execute(thread);
	}
    /**
     * tcp异步调用
     * 
     * @param thread 异步任务
     */
    public void tcpExecute(Runnable thread) {
    	tcpExecutor.execute(thread);
    }
    /**
     * 循环定时调用
     * @Title: scheduleExecute 
     * @Description: 循环定时调用
     * @param task 定时任务
     * @param delay 初次执行的延迟
     * @param period 循环周期
     * @param tUnit 定时单位
     * @return ScheduledFuture<?> 返回类型 
     * @throws 抛出错误
     * @author 10155 
     * @date 2020年3月19日 下午10:35:00
     */
    public ScheduledFuture<?> scheduleExecute(TimerTask task,long delay,long period,TimeUnit tUnit) {
		return executor.scheduleAtFixedRate(task, delay, period, tUnit);
	}
}
