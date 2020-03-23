package com.audioweb.server;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
/**
 * netty服务启动与调用类
 * @ClassName: ServerManager 
 * @Description: netty服务启动与调用类
 * @author 10155 hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2020年3月23日 下午10:40:22
 */
@Component
public class ServerManager {
	
	@Bean(name="ioNettyServer",initMethod="startServer",destroyMethod="destory")
	public IoNettyServer getIoService() {
		return new IoNettyServer();
	}
	
	@Bean(name="tcpNettyServer",initMethod="startServer",destroyMethod="destory")
	public TcpNettyServer getTcpService() {
		return new TcpNettyServer();
	}
	
	@Bean(name="loginNettyServer",initMethod="startServer",destroyMethod="destory")
	public LoginNettyServer getLoginService() {
		return new LoginNettyServer();
	}
}
