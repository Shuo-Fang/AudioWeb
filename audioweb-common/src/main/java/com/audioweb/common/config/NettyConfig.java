/**   
 * @Title: NettyConfig.java 
 * @Package com.audioweb.common.config 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com
 * @date 2020年3月21日 下午2:16:00 
 * @version V1.0   
 */ 
package com.audioweb.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/** 
 * @ClassName: NettyConfig 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2020年3月21日 下午2:16:00  
 */
@Component
@ConfigurationProperties(prefix = "netty")
public class NettyConfig {
	private static String serverIp;
	
	private static Integer serverPort;
	
	private static Integer loginPort;
	
	private static Integer qtClientPort;

	public static String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
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
}
