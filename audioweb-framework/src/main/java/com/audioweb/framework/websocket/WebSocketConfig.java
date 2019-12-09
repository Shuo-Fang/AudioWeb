package com.audioweb.framework.websocket;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import com.audioweb.framework.util.ShiroUtils;
/**
 * 初始化配置WebSocket功能
 * @ClassName: WebSocketConfig 
 * @Description: TODO(初始化配置WebSocket功能) 
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2019年12月9日 上午11:22:58
 */
@Configuration
public class WebSocketConfig  extends ServerEndpointConfig.Configurator{
    
	@Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
    
	@Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        // 将用户信息存储到socket的配置里
        sec.getUserProperties().put("user", ShiroUtils.getSysUser());
        super.modifyHandshake(sec, request, response);
    }
}