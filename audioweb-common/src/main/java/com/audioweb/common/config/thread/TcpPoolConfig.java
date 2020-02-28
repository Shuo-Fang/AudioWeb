/**   
 * @Title: TcpPoolConfig.java 
 * @Package com.audioweb.common.config.thread 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com
 * @date 2020年2月28日 上午10:24:38 
 * @version V1.0   
 */ 
package com.audioweb.common.config.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.audioweb.common.utils.Threads;

/** 
 * @ClassName: TcpPoolConfig 
 * @Description: tcp调用线程池
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2020年2月28日 上午10:24:38  
 */
@Configuration
public class TcpPoolConfig implements PoolConfig{
	/**
	 * tcp调用线程池
	 * @Title: tcpServiceExecutor 
	 * @Description: tcp调用线程池
	 * @return Executor 返回类型 
	 * @throws 抛出错误
	 * @author ShuoFang 
	 * @date 2020年2月28日 上午10:13:32
	 */
    @Bean(name = "TcpServiceExecutor")
    public Executor tcpServiceExecutor() {
        log.info("start TcpServiceExecutor");
        return new ThreadPoolExecutor(NUMBER_OF_CORES,
				NUMBER_OF_CORES*2, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT,
				new LinkedBlockingQueue<Runnable>(1024),
				new BasicThreadFactory.Builder().namingPattern("tcp-pool-%d").daemon(true).build(),
				new ThreadPoolExecutor.AbortPolicy()) {
        	@Override
            protected void afterExecute(Runnable r, Throwable t)
            {
                super.afterExecute(r, t);
                Threads.printException(r, t);
            }
        };
    }
}
