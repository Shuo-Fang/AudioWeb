/**   
 * @Title: UserUuidCheckException.java 
 * @Package com.audioweb.common.exception.user 
 * @Description: 用户登录接口信息异常
 * @author ShuoFang 1015510750@qq.com
 * @date 2020年6月26日 下午1:47:41 
 * @version V1.0   
 */ 
package com.audioweb.common.exception.user;

/** 
 * @ClassName: UserUuidCheckException 
 * @Description: 用户uuid获取出错或失败
 * @author ShuoFang 1015510750@qq.com 
 * @date 2020年6月26日 下午1:47:41  
 */
public class UserUuidCheckException extends UserException{
	private static final long serialVersionUID = 1L;

    public UserUuidCheckException()
    {
        super("user.uuid.check", null);
    }
}
