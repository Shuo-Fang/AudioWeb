/**   
 * @Title: SpringBeanServicePool.java 
 * @Package com.audioweb.server.service 
 * @Description: 终端管理获取spring服务的单一静态库
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com
 * @date 2020年3月21日 下午3:45:24 
 * @version V1.0   
 */ 
package com.audioweb.server.service;

import com.audioweb.common.utils.spring.SpringUtils;
import com.audioweb.work.service.impl.WorkTerminalServiceImpl;

/** 
 * @ClassName: SpringBeanServicePool 
 * @Description: 终端管理获取spring服务的单一静态库
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2020年3月21日 下午3:45:24  
 */
public class SpringBeanServicePool {
	private WorkTerminalServiceImpl terminalServiceImpl = SpringUtils.getBean(WorkTerminalServiceImpl.class);
	private SpringBeanServicePool() {}
	private static SpringBeanServicePool service = new SpringBeanServicePool();
	
	public static SpringBeanServicePool getService() {
		return service;
	}

	public WorkTerminalServiceImpl getTerminalServiceImpl() {
		return terminalServiceImpl;
	}
}
