package com.audioweb.framework.websocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;

import com.audioweb.common.json.JSONObject;
import com.audioweb.system.domain.SysUser;
import com.audioweb.system.service.impl.SysUserServiceImpl;

/**
 * 总信息管理的socket处理
 * @ClassName: MessageSocket 
 * @Description: TODO(总信息管理的socket处理) 
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2019年12月9日 上午11:22:41
 */
@ServerEndpoint(value = "/websocket/message", configurator = WebSocketConfig.class)
@Component
public class MessageSocket {

	private static ApplicationContext applicationContext;
	private static int onlineCount = 0;
	private static CopyOnWriteArraySet<MessageSocket> webSocketSet = new CopyOnWriteArraySet<>();
	private Session session;
	private SysUserServiceImpl userService;
	// todo 这里需要一个变量来接收shiro中登录的人信息
	private SysUser shiroUser;

	@OnOpen
	public void onOpen(Session session) {
		this.session = session;
		// 注入userService
		this.userService = applicationContext.getBean(SysUserServiceImpl.class);
		// 设置用户
		this.shiroUser = (SysUser) session.getUserProperties().get("user");
		webSocketSet.add(this);
		addOnlineCount();
		System.out.println("有新链接加入!当前在线人数为" + getOnlineCount());
	}

	@OnClose
	public void onClose() {
		webSocketSet.remove(this);
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
	 * @throws
	 */
	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		// TODO
		JSONObject jsonObject = new JSONObject();
		if(message != null && message.equals("time")) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			jsonObject.put("type", message);
			jsonObject.put("data", df.format(new Date()));
			sendMessage(jsonObject.toCompactString());
		}else {
			jsonObject.put("type", message);
			jsonObject.put("data", "100");
			sendInfo(jsonObject.toCompactString());
		}
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
	@OnMessage
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

    }
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
	public void sendInfo(String text) throws IOException {
		for (MessageSocket item : webSocketSet) {
			try {
				item.sendMessage(text);
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

	public static void setApplicationContext(ApplicationContext applicationContext) {
		MessageSocket.applicationContext = applicationContext;
	}
}