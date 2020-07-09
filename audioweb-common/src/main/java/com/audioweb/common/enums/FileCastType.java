/**   
 * @Title: FileCastType.java 
 * @Package com.audioweb.common.enums 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author ShuoFang 1015510750@qq.com
 * @date 2020年3月19日 上午10:49:22 
 * @version V1.0   
 */ 
package com.audioweb.common.enums;

import com.alibaba.fastjson.annotation.JSONType;
import com.audioweb.common.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

/** 文件广播的类型
 * @ClassName: FileCastType 
 * @Description: 文件广播的类型
 * @author ShuoFang 1015510750@qq.com 
 * @date 2020年3月19日 上午10:49:22  
 */

@JSONType(serializeEnumAsJavaBean = true)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FileCastType{
	ORDER("顺序播放"),LIST("列表循环"),RANDOM("随机播放"),SINGLE("单曲循环");
	 /**
     * 枚举值码
     */
	@JsonValue
    private final String code;
 
	private FileCastType(String code) {
		this.code = code;
	}
    public String getCode()
    {
        return code;
    }
    
    public static FileCastType invokeEnum(String name) {
    	if(StringUtils.isEmpty(name)) {
    		return null;
    	}
    	FileCastType[] types = FileCastType.values();
		//遍历查找
	    for(FileCastType s : types){
           if(s.name().equals(name) || s.getCode().equals(name)){
               return s;
           }
	    }
		return null;
	}
}
