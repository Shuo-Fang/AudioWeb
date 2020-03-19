/**   
 * @Title: FileCastType.java 
 * @Package com.audioweb.common.enums 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com
 * @date 2020年3月19日 上午10:49:22 
 * @version V1.0   
 */ 
package com.audioweb.common.enums;

import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonFormat;

/** 文件广播的类型
 * @ClassName: FileCastType 
 * @Description: 文件广播的类型
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2020年3月19日 上午10:49:22  
 */

@JSONType(serializeEnumAsJavaBean = true)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FileCastType{
	ORDER("ORDER","顺序播放"),LIST("LIST","列表循环"),RANDOM("RANDOM","随机播放");
	 /**
     * 枚举值码
     */
    private final String code;
 
    /**
     * 枚举描述
     */
    private final String message;
	private FileCastType(String code,String message) {
		this.code = code;
		this.message = message;
	}
    public String getCode()
    {
        return code;
    }
	public String getMessage() {
		return message;
	}
}
