/**   
 * @Title: SysAuthorization.java 
 * @Package com.audioweb.system.domain 
 * @Description: 接口登录身份证明
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com
 * @date 2020年2月26日 下午1:52:19 
 * @version V1.0   
 */ 
package com.audioweb.system.domain;

import java.util.LinkedList;
import java.util.UUID;

/** 
 * @ClassName: SysAuthorization 
 * @Description: 接口登录身份证明
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2020年2月26日 下午1:52:19  
 */
public class SysAuthorization {
	private static LinkedList<String> authorizations = new LinkedList<>();
	
	private static Integer MAXCHECK = 20;
	
	public static String getAuthorization() {
		String uuid = UUID.randomUUID().toString().replace("-", "");
    	synchronized (authorizations) {
        	if(authorizations.size() < MAXCHECK) {
        		authorizations.addFirst(uuid);
        	}else {
        		authorizations.removeLast();
        		authorizations.addFirst(uuid);
        	}
		}
    	return uuid;
	}
	
	public static boolean checkAuthorization(String uuid) {
		synchronized (authorizations) {
			return authorizations.remove(uuid);
		}
	}
}
