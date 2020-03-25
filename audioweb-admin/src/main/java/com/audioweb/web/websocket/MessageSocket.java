package com.audioweb.web.websocket;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.audioweb.common.json.JSONObject;
import com.audioweb.common.utils.spring.SpringUtils;
import com.audioweb.server.NettyBase;
import com.audioweb.system.domain.SysUser;
import com.audioweb.system.service.impl.SysUserServiceImpl;
import com.audioweb.work.global.WebsocketGlobal;

/**
 * 总信息管理的socket处理
 * @ClassName: MessageSocket 
 * @Description: TODO(总信息管理的socket处理) 
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2019年12月9日 上午11:22:41
 */
@ServerEndpoint(value = "/websocket/message", configurator = WebSocketConfig.class)
@Component
//war部署时此注解@Component需要注释掉
public class MessageSocket {
	private static final Logger  log = LoggerFactory.getLogger(MessageSocket.class);
	private static int onlineCount = 0;
	private static CopyOnWriteArraySet<MessageSocket> webSocketSet = new CopyOnWriteArraySet<>();
	private Session session;
	private String onlineSessionId;
	private SysUserServiceImpl userService;
	// todo 这里需要一个变量来接收shiro中登录的人信息
	private SysUser shiroUser;
	
	
	@OnOpen
	public void onOpen(Session session) {
		this.session = session;
		// 注入userService
		this.userService = SpringUtils.getBean(SysUserServiceImpl.class);
		this.onlineSessionId = session.getUserProperties().get("sessionId").toString();
		WebsocketGlobal.putMessageId(onlineSessionId);//存入全局信息中
		// 设置用户
		this.shiroUser = (SysUser) session.getUserProperties().get("user");
		webSocketSet.add(this);
		addOnlineCount();
		System.out.println("有新链接加入!当前在线人数为" + getOnlineCount());
	}

	@OnClose
	public void onClose() {
		webSocketSet.remove(this);
		WebsocketGlobal.removeMessageId(onlineSessionId);//从全局信息中移除
		subOnlineCount();
		System.out.println("有一链接关闭!当前在线人数为" + getOnlineCount());
	}
	/**
	 * 
	 * @Title: onMessage 
	 * @Description: TODO(接送客户端发送的字符串信息) 
	 * @param @param message
	 * @param @param session
	 * @param @throws IOException   
	 * @return void 返回类型 
	 * @author ShuoFang 
	 * @date 2019年12月9日 下午1:14:40
	 * @throws IOException
	 */
	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		// TODO
		JSONObject jsonObject = new JSONObject();
		if(message != null && message.equals("time")) {
			//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			jsonObject.put("type", message);
			jsonObject.put("data", System.currentTimeMillis());
			sendMessage(jsonObject.toCompactString());
		}else {
			jsonObject.put("type", message);
			jsonObject.put("data", "100");
			sendMessage(jsonObject.toCompactString());
			//sendInfo(jsonObject.toCompactString());
		}
	}
	
	@OnError
    public void onError(Throwable t) throws Throwable {
		log.error("MessageSocket Error: ",t);
    }
	/**
	 * 
	 * @Title: onMessage 
	 * @Description: TODO(接收客户端发送的字节流信息) 
	 * @param @param messages
	 * @param @param session   
	 * @return void 返回类型 
	 * @author ShuoFang 
	 * @date 2019年12月9日 下午1:15:07
	 * @throws
	 */
/*	@OnMessage
    public void onMessage(byte[] messages, Session session) {
        try {
            System.out.println("接收到消息:"+new String(messages,"utf-8"));
            //返回信息
            String resultStr="{name:\"张三\",age:18,addr:\"上海浦东\"}";
            //发送字符串信息的 byte数组
            ByteBuffer bf=ByteBuffer.wrap(resultStr.getBytes("utf-8"));
            session.getBasicRemote().sendBinary(bf);
            //发送字符串
            //session.getBasicRemote().sendText("测试");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }*/
	/*@OnBinary
	public void onMessagedata(BinaryMessage data, Session session) throws IOException {
		// TODO
		//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sendMessage("blob测试");
	}*/
	
	public void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
	}

	/**
	 * 群发自定义消息
	 */
	public static void sendInfo(String tyep,String data) throws IOException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("type", tyep);
		jsonObject.put("data", data);
		for (MessageSocket item : webSocketSet) {
			try {
				item.sendMessage(jsonObject.toCompactString());
			} catch (IOException e) {
				continue;
			}
		}
	}
	/**
	 * 群发刷新页面消息
	 */
	public static void refreshInfo(String menuId) throws IOException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("type", "refresh");
		jsonObject.put("data", menuId);
		for (MessageSocket item : webSocketSet) {
			try {
				item.sendMessage(jsonObject.toCompactString());
			} catch (IOException e) {
				continue;
			}
		}
	}

	public static synchronized int getOnlineCount() {
		return MessageSocket.onlineCount;
	}

	public static synchronized void addOnlineCount() {
		MessageSocket.onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		MessageSocket.onlineCount--;
	}
}