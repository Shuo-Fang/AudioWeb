/**   
 * @Title: TimeFileCast.java 
 * @Package com.audioweb.server.service 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com
 * @date 2020年4月13日 下午5:24:39 
 * @version V1.0   
 */ 
package com.audioweb.server.service;

import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import com.audioweb.work.domain.FileCastTask;

/** 
 * @ClassName: TimeFileCast 
 * @Description: 文件广播定时器信息
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2020年4月13日 下午5:24:39  
 */
public class TimeFileCast extends TimerTask{
	
	/**定时器身份证明*/
	ScheduledFuture<?> castCard;
	
	/**定时器关联任务*/
	FileCastTask task;
	public TimeFileCast(FileCastTask task) {
		this.task = task;
		
	}
	
	@Override
	public void run() {
		
	}
}
