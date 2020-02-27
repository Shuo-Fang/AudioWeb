package com.audioweb.web.websocket;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import com.audioweb.common.global.WebsocketGlobal;
import com.audioweb.common.utils.spring.SpringUtils;
import com.audioweb.system.domain.SysUser;
import com.audioweb.system.service.impl.SysUserServiceImpl;

/**
 * 实时广播中的socket连接
 * @ClassName: MessageSocket 
 * @Description: TODO(实时广播中的socket连接) 
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2019年12月9日 下午13:32:41
 */
@ServerEndpoint(value = "/websocket/realtime", configurator = WebSocketConfig.class)
@Component
//war部署时此注解@Component需要注释掉
public class BlobSocket {
	private static int onlineCount = 0;
	private static CopyOnWriteArraySet<BlobSocket> webSocketSet = new CopyOnWriteArraySet<>();
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
		WebsocketGlobal.putAppId(onlineSessionId);//存入全局信息中
		// 设置用户
		this.shiroUser = (SysUser) session.getUserProperties().get("user");
		webSocketSet.add(this);
		addOnlineCount();
		System.out.println("有新链接加入!当前广播人数" + getOnlineCount());
	}

	@OnClose
	public void onClose() {
		webSocketSet.remove(this);
		subOnlineCount();
		System.out.println("有一链接关闭!当前广播人数为" + getOnlineCount());
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
	 */
	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		// TODO
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sendMessage(df.format(new Date()));
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
	 */
	@OnMessage
    public void onMessage(byte[] messages, Session session) {
        try {
            System.out.println("接收到消息:"+new String(messages,"utf-8"));
            //TODO
           /* String resultStr="{name:\"张三\",age:18,addr:\"上海浦东\"}";
            //发送字符串信息的 byte数组
            ByteBuffer bf=ByteBuffer.wrap(resultStr.getBytes("utf-8"));
            session.getBasicRemote().sendBinary(bf);*/
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
	
	public void sendMessage(String message) throws IOException {
		// String m = "1,2,3,4,5";
		this.session.getBasicRemote().sendText(message);
	}

	/**
	 * 群发自定义消息
	 */
	/*public void sendInfo(String text) throws IOException {
		for (BlobSocket item : webSocketSet) {
			try {
				item.sendMessage(text);
			} catch (IOException e) {
				continue;
			}
		}
	}
*/
	public static synchronized int getOnlineCount() {
		return BlobSocket.onlineCount;
	}

	public static synchronized void addOnlineCount() {
		BlobSocket.onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		BlobSocket.onlineCount--;
	}
}