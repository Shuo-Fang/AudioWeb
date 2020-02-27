/**   
 * @Title: WebsocketGlobal.java 
 * @Package com.audioweb.common.global 
 * @Description:  websocket 全局信息管理
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com
 * @date 2020年2月27日 下午12:03:28 
 * @version V1.0   
 */ 
package com.audioweb.common.global;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/** 
 * @ClassName: WebsocketGlobal 
 * @Description: websocket 全局信息管理
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2020年2月27日 下午12:03:28  
 */
public class WebsocketGlobal {
	private static Set<String> appSessionIds = new HashSet<String>();
	private static Set<String> messageSessionIds = new HashSet<String>();
	private static Set<String> BlobSessionIds = new HashSet<String>();
	/**
	 * 存储会话sessionID
	 * @Title: putAppId 
	 * @Description: 存储会话sessionID
	 * @param sessionId
	 * @return boolean 返回类型 
	 * @throws 抛出错误
	 * @author ShuoFang 
	 * @date 2020年2月27日 下午1:15:14
	 */
	public static boolean putAppId(String sessionId) {
		synchronized (appSessionIds) {
			return appSessionIds.add(sessionId);
		}
	}
	/**
	 * 存储会话sessionID
	 * @Title: putMessageId 
	 * @Description: 存储会话sessionID
	 * @param sessionId
	 * @return boolean 返回类型 
	 * @throws 抛出错误
	 * @author ShuoFang 
	 * @date 2020年2月27日 下午1:15:41
	 */
	public static boolean putMessageId(String sessionId) {
		synchronized (messageSessionIds) {
			return messageSessionIds.add(sessionId);
		}
	}
	/**
	 * 存储会话sessionID
	 * @Title: putBlobId 
	 * @Description: 存储会话sessionID
	 * @param sessionId
	 * @return boolean 返回类型 
	 * @throws 抛出错误
	 * @author ShuoFang 
	 * @date 2020年2月27日 下午1:15:47
	 */
	public static boolean putBlobId(String sessionId) {
		synchronized (BlobSessionIds) {
			return BlobSessionIds.add(sessionId);
		}
	}
	/**
	 * 移除会话sessionID
	 * @Title: removeAppId 
	 * @Description: 移除会话sessionID
	 * @param sessionId
	 * @return boolean 返回类型 
	 * @throws 抛出错误
	 * @author ShuoFang 
	 * @date 2020年2月27日 下午1:15:55
	 */
	public static boolean removeAppId(String sessionId) {
		synchronized (appSessionIds) {
			return appSessionIds.remove(sessionId);
		}
	}
	/**
	 * 移除会话sessionID
	 * @Title: removeMessageId 
	 * @Description: 移除会话sessionID
	 * @param sessionId
	 * @return boolean 返回类型 
	 * @throws 抛出错误
	 * @author ShuoFang 
	 * @date 2020年2月27日 下午1:16:07
	 */
	public static boolean removeMessageId(String sessionId) {
		synchronized (messageSessionIds) {
			return messageSessionIds.remove(sessionId);
		}
	}
	/**
	 * 移除会话sessionID
	 * @Title: removeBlobId 
	 * @Description: 移除会话sessionID
	 * @param sessionId
	 * @return boolean 返回类型 
	 * @throws 抛出错误
	 * @author ShuoFang 
	 * @date 2020年2月27日 下午1:16:13
	 */
	public static boolean removeBlobId(String sessionId) {
		synchronized (BlobSessionIds) {
			return BlobSessionIds.remove(sessionId);
		}
	}
	/**
	 * 获取全部连接WebSocket的sessionID
	 * @Title: getAllIds 
	 * @Description: 获取全部连接WebSocket的sessionID
	 * @return ArrayList<String> 返回类型 
	 * @throws 抛出错误
	 * @author ShuoFang 
	 * @date 2020年2月27日 下午1:46:59
	 */
	public static ArrayList<String> getAllIds() {
		Set<String> all = new HashSet<String>();
		all.addAll(BlobSessionIds);
		all.addAll(messageSessionIds);
		all.addAll(appSessionIds);
		return new ArrayList<String>(all);
	}
}
