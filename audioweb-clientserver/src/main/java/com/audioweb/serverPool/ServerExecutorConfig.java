package com.audioweb.serverPool;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.audioweb.common.config.Global;
import com.audioweb.common.config.datasource.DynamicDataSourceContextHolder;

/** 
 * 配置终端通信线程池
 * @ClassName: IOExecutorConfig 
 * @Description: TODO(配置终端通信线程池) 
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2019年12月9日 下午4:41:19
 */
@EnableAsync
@Configuration
public class ServerExecutorConfig {
	/**
     *  获取活跃的 cpu数量
     */
    private final static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    public static final Logger log = LoggerFactory.getLogger(DynamicDataSourceContextHolder.class);

	    @Bean(name = "ServerServiceExecutor")
	    public Executor asyncServiceExecutor() {
	        log.info("start IOServiceExecutor");
	        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	        //ThreadPoolTaskExecutor executor = new VisiableThreadPoolTaskExecutor();
	        //配置核心线程数
	        executor.setCorePoolSize(NUMBER_OF_CORES/2);
	        //配置最大线程数
	        executor.setMaxPoolSize(NUMBER_OF_CORES*2);
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
}