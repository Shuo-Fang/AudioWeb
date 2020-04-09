/**   
 * @Title: IoPoolConfig.java 
 * @Package com.audioweb.common.config.thread 
 * @Description: 终端IO线程池
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com
 * @date 2020年2月28日 上午10:39:46 
 * @version V1.0   
 */ 
package com.audioweb.common.config.thread;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.audioweb.common.utils.Threads;

/** 
 * @ClassName: IoPoolConfig 
 * @Description: 终端IO线程池
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2020年2月28日 上午10:39:46  
 */
@Configuration
public class IoPoolConfig implements PoolConfig {
	/**
	 * 
	 * @Title: ioServiceExecutor 
	 * @Description: 终端IO线程池
	 * @return Executor 返回类型 
	 * @throws 抛出错误
	 * @author ShuoFang 
	 * @date 2020年2月28日 上午10:40:34
	 */
	@Bean(name = "IoServiceExecutor")
    public ExecutorService ioServiceExecutor() {
        log.info("start IoServiceExecutor");
        return new ThreadPoolExecutor(NUMBER_OF_CORES,
				NUMBER_OF_CORES*8, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT,
				new LinkedBlockingQueue<Runnable>(1024),
				new MyThreadFactory(),
				new ThreadPoolExecutor.AbortPolicy()) {
        	@Override
            protected void afterExecute(Runnable r, Throwable t)
            {
                super.afterExecute(r, t);
                Threads.printException(r, t);
            }
        };
    }

    private class MyThreadFactory implements ThreadFactory {
        private AtomicInteger threadNumberAtomicInteger = new AtomicInteger(1);
        private String name = "io-pool-";
        @Override
        public Thread newThread(Runnable r) {
            Thread thread=  new Thread(r,String.format(Locale.CHINA,"%s%d",name,threadNumberAtomicInteger.getAndIncrement()));
            /* thread.setDaemon(true);//是否是守护线程*/
            thread.setPriority(Thread.NORM_PRIORITY+1);//设置优先级 1~10 有3个常量 默认 Thread.MIN_PRIORITY
            return thread;
        }
    }
}

