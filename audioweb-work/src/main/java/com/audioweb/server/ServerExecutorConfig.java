package com.audioweb.server;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import com.audioweb.common.config.datasource.DynamicDataSourceContextHolder;

import io.netty.util.NettyRuntime;
import io.netty.util.internal.SystemPropertyUtil;

/** 
 * 配置终端通信以及定时任务线程池
 * @ClassName: IOExecutorConfig 
 * @Description: 配置终端通信以及定时任务线程池
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2019年12月9日 下午4:41:19
 */
@Configuration
@EnableScheduling
public class ServerExecutorConfig implements AsyncConfigurer{
	/**
     *  获取活跃的 cpu数量
     */
    private final static int NUMBER_OF_CORES = Math.max(1, SystemPropertyUtil.getInt(
            "io.netty.eventLoopThreads", NettyRuntime.availableProcessors()));
    public static final Logger  log = LoggerFactory.getLogger(DynamicDataSourceContextHolder.class);
    	/**
    	 * 作为前端调用处理线程池
    	 * @Title: asyncServiceExecutor 
    	 * @Description: 作为前端调用处理线程池
    	 * @return Executor 返回类型 
    	 * @throws 抛出错误
    	 * @author ShuoFang 
    	 * @date 2020年1月20日 下午2:34:24
    	 */
	    @Bean(name = "ServerServiceExecutor")
	    public Executor asyncServiceExecutor() {
	        log.info("start ServerServiceExecutor");
	        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	        //配置核心线程数
	        executor.setCorePoolSize(NUMBER_OF_CORES/2>0?NUMBER_OF_CORES/2:1);
	        //配置最大线程数
	        executor.setMaxPoolSize(NUMBER_OF_CORES);
	        //配置队列大小 不做配置
	        //executor.setQueueCapacity(queueCapacity);
	        //配置线程池中的线程的名称前缀
	        executor.setThreadNamePrefix("server-thread-");
	        //配置空闲线程kill时间
	        executor.setKeepAliveSeconds(NUMBER_OF_CORES*2);
	        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
	        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行 
	        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
	        //执行初始化
	        executor.initialize();
	        return executor;
	    }
	    /**
	     * netty Boss 共享线程池
	     * @Title: bossServiceExecutor 
	     * @Description: TODO(这里用一句话描述这个方法的作用) 
	     * @return Executor 返回类型 
	     * @throws 抛出错误
	     * @author ShuoFang 
	     * @date 2020年1月20日 下午2:36:06
	     */
	    @Bean(name = "BossServiceExecutor")
	    public Executor bossServiceExecutor() {
	        log.info("start ServerServiceExecutor");
	        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	        //配置核心线程数
	        executor.setCorePoolSize(NUMBER_OF_CORES);
	        //配置最大线程数
	        executor.setMaxPoolSize(NUMBER_OF_CORES*4);
	        //配置队列大小 不做配置
	        //executor.setQueueCapacity(queueCapacity);
	        //配置线程池中的线程的名称前缀
	        executor.setThreadNamePrefix("boss-thread-");
	        //配置空闲线程kill时间
	        executor.setKeepAliveSeconds(NUMBER_OF_CORES*4);
	        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
	        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行 
	        //executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
	        //执行初始化
	        executor.initialize();
	        return executor;
	    }
	    /**
	     * netty IO通信线程池
	     * @Title: ioServiceExecutor
	     * @Description: TODO(这里用一句话描述这个方法的作用) 
	     * @return Executor 返回类型 
	     * @throws 抛出错误
	     * @author ShuoFang 
	     * @date 2020年1月20日 下午3:18:19
	     */
	    @Bean(name = "IoServiceExecutor")
	    public Executor ioServiceExecutor() {
	        log.info("start IoServiceExecutor");
	        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	        //配置核心线程数
	        executor.setCorePoolSize(NUMBER_OF_CORES*2);
	        //配置最大线程数
	        executor.setMaxPoolSize(NUMBER_OF_CORES*4);
	        //配置队列大小 不做配置
	        //executor.setQueueCapacity(queueCapacity);
	        //配置线程池中的线程的名称前缀
	        executor.setThreadNamePrefix("io-thread-");
	        //配置空闲线程kill时间
	        executor.setKeepAliveSeconds(NUMBER_OF_CORES*4);
	        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
	        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行 
	        //executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
	        //执行初始化
	        executor.initialize();
	        return executor;
	    }
	    @Override
	    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
	        return (throwable, method, objects) -> {
	            log.error("异步任务执行出现异常, message {}, emthod {}, params {}", throwable, method, objects);
	        };
	    }
		
}