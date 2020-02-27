/**   
 * @Title: AppSocket.java 
 * @Package com.audioweb.framework.websocket 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com
 * @date 2020年2月25日 上午10:25:23 
 * @version V1.0   
 */ 
package com.audioweb.web.websocket;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.audioweb.common.global.WebsocketGlobal;
import com.audioweb.common.json.JSONObject;
import com.audioweb.common.utils.spring.SpringUtils;
import com.audioweb.framework.shiro.session.OnlineSession;
import com.audioweb.framework.shiro.session.OnlineSessionDAO;
import com.audioweb.framework.shiro.web.session.SpringSessionValidationScheduler;
import com.audioweb.system.domain.SysUser;
import com.audioweb.system.service.impl.SysUserServiceImpl;

/** 
 * @ClassName: AppSocket 
 * @Description: App端socket连接
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2020年2月25日 上午10:25:23  
 */
@ServerEndpoint(value = "/websocket/app", configurator = WebSocketConfig.class)
@Component
//war部署时此注解@Component需要注释掉
public class AppSocket {
	private static int onlineCount = 0;
	private static CopyOnWriteArraySet<AppSocket> webSocketSet = new CopyOnWriteArraySet<>();
	private Session session;
	private String onlineSessionId;
	// todo 这里需要一个变量来接收shiro中登录的人信息
	private SysUser shiroUser;
	
	private static final Logger log = LoggerFactory.getLogger(AppSocket.class);

    // 相隔多久检查一次session的有效性，单位毫秒，默认就是10分钟
    @Value("${shiro.session.validationInterval}")
    private long sessionValidationInterval;
    
    /**
     * 定时器，用于防止http超时而挂起请求。
     */
    @Autowired
    @Qualifier("scheduledExecutorService")
    private ScheduledExecutorService executorService;

    @Autowired
    private OnlineSessionDAO onlineSessionDAO;
    
    
	/** 
	 * <p>Title: </p> 
	 * <p>Description: </p> 
	 * @author ShuoFang
	 * @date 2020年2月27日 上午10:36:55 
	 */
	public AppSocket() {
		// TODO Auto-generated constructor stub
		/*try
        {
            executorService.scheduleAtFixedRate(new Runnable()
            {
                @Override
                public void run()
                {
                	for(AppSocket socket:webSocketSet) {
                		//socket.
                	}
                }
            }, 1000, sessionValidationInterval * 60 * 1000, TimeUnit.MILLISECONDS);
            if (log.isDebugEnabled())
            {
                log.debug("Websocket Session validation job successfully scheduled with Spring Scheduler.");
            }

        }
        catch (Exception e)
        {
            if (log.isErrorEnabled())
            {
                log.error("Error starting the Spring Scheduler Websocket session validation job. Websocket Session validation may not occur.", e);
            }
        }*/
	}
	@OnOpen
	public void onOpen(Session session) {
		this.session = session;
		this.onlineSessionId = session.getUserProperties().get("sessionId").toString();
		WebsocketGlobal.putAppId(onlineSessionId);//存入全局信息中
		// 设置用户
		this.shiroUser = (SysUser) session.getUserProperties().get("user");
		webSocketSet.add(this);
		addOnlineCount();
		System.out.println("App有新链接加入!当前在线人数为" + getOnlineCount());
	}

	@OnClose
	public void onClose() {
		webSocketSet.remove(this);
		WebsocketGlobal.removeAppId(onlineSessionId);//从全局信息中移除
		subOnlineCount();
		System.out.println("App有一链接关闭!当前在线人数为" + getOnlineCount());
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
	public void sendInfo(String text) throws IOException {
		for (AppSocket item : webSocketSet) {
			try {
				item.sendMessage(text);
			} catch (IOException e) {
				continue;
			}
		}
	}

	public static synchronized int getOnlineCount() {
		return AppSocket.onlineCount;
	}

	public static synchronized void addOnlineCount() {
		AppSocket.onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		AppSocket.onlineCount--;
	}
}
