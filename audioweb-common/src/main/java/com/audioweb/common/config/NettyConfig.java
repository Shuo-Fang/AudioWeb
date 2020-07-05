/**   
 * @Title: NettyConfig.java 
 * @Package com.audioweb.common.config 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author ShuoFang 1015510750@qq.com
 * @date 2020年3月21日 下午2:16:00 
 * @version V1.0   
 */ 
package com.audioweb.common.config;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.audioweb.common.constant.WorkConstants;
import com.audioweb.common.utils.IpUtils;

/** 
 * @ClassName: NettyConfig 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author ShuoFang 1015510750@qq.com 
 * @date 2020年3月21日 下午2:16:00  
 */
@Component
@ConfigurationProperties(prefix = "netty")
public class NettyConfig {
	private static String serverIp;
	
	private static Integer serverPort;
	
	private static Integer loginPort;
	
	private static Integer qtClientPort;
	
	private static Integer terRecPort;

	private static AtomicLong adress = new AtomicLong(IpUtils.ip2Long(WorkConstants.WORK_ADRESS));
	
	private static AtomicInteger groupPort = new AtomicInteger(WorkConstants.WORK_PORT);
	
	public static String getServerIp() {
		return serverIp;
	}

	public static void setServerIp(String serverIp) {
		NettyConfig.serverIp = serverIp;
	}

	public static Integer getServerPort() {
		return serverPort;
	}

	public void setServerPort(Integer serverPort) {
		NettyConfig.serverPort = serverPort;
	}

	public static Integer getLoginPort() {
		return loginPort;
	}

	public void setLoginPort(Integer loginPort) {
		NettyConfig.loginPort = loginPort;
	}

	public static Integer getQtClientPort() {
		return qtClientPort;
	}

	public void setQtClientPort(Integer qtClientPort) {
		NettyConfig.qtClientPort = qtClientPort;
	}

	public static String getAdress() {
		//是否大于"231.255.255.255"
		if(adress.get() >= 3892314111L) {
			synchronized (adress) {
				adress = new AtomicLong(IpUtils.ip2Long(WorkConstants.WORK_ADRESS));
				return IpUtils.long2Ip(adress.getAndIncrement());
			}
		}
		return IpUtils.long2Ip(adress.getAndIncrement());
	}

	public static Integer getGroupPort() {
		if(groupPort.get() >= 65535) {
			synchronized (groupPort) {
				groupPort = new AtomicInteger(WorkConstants.WORK_PORT);
				return groupPort.getAndIncrement();
			}
		}
		return groupPort.getAndIncrement();
	}

	public static Integer getTerRecPort() {
		return terRecPort;
	}

	public void setTerRecPort(Integer terRecPort) {
		NettyConfig.terRecPort = terRecPort;
	}
}
