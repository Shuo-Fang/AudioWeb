/**   
 * @Title: BossPoolConfig.java 
 * @Package com.audioweb.common.config.thread 
 * @Description: netty boss调用线程池
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com
 * @date 2020年2月28日 上午10:36:10 
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
 * @ClassName: BossPoolConfig 
 * @Description: netty boss调用线程池
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2020年2月28日 上午10:36:10  
 */
@Configuration
public class BossPoolConfig implements PoolConfig{
	@Bean(name = "BossServiceExecutor")
    public Executor bossServiceExecutor() {
        log.info("start BossServiceExecutor");
        return new ThreadPoolExecutor(NUMBER_OF_CORES,
				NUMBER_OF_CORES*4, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT,
				new LinkedBlockingQueue<Runnable>(1024),
				new BasicThreadFactory.Builder().namingPattern("boss-pool-%d").daemon(true).build(),
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
