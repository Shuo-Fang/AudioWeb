/**   
 * @Title: UserInterfaceCheckException.java 
 * @Package com.audioweb.common.exception.user 
 * @Description: 用户登录接口信息异常
 * @author ShuoFang 1015510750@qq.com
 * @date 2020年2月26日 下午1:47:41 
 * @version V1.0   
 */ 
package com.audioweb.common.exception.user;

/** 
 * @ClassName: UserInterfaceCheckException 
 * @Description: 用户登录接口信息异常
 * @author ShuoFang 1015510750@qq.com 
 * @date 2020年2月26日 下午1:47:41  
 */
public class UserInterfaceCheckException extends UserException{
	private static final long serialVersionUID = 1L;

    public UserInterfaceCheckException()
    {
        super("user.interface.check", null);
    }
}
