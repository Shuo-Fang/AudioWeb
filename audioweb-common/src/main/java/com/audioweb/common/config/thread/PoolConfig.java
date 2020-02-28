/**   
 * @Title: PoolConfig.java 
 * @Package com.audioweb.common.config.thread 
 * @Description: 通用线程池配置参数 
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com
 * @date 2020年2月28日 上午10:33:35 
 * @version V1.0   
 */ 
package com.audioweb.common.config.thread;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * @ClassName: PoolConfig 
 * @Description: 通用线程池配置参数 
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2020年2月28日 上午10:33:35  
 */
public interface PoolConfig {
    public int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();//获取活跃的 cpu数量
    public long KEEP_ALIVE_TIME = 3L; //线程活跃时间
    public TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS; //线程活跃时间单位
    public static final Logger  log = LoggerFactory.getLogger(PoolConfig.class);
	
}
