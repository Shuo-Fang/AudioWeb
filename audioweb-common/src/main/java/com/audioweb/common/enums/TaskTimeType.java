package com.audioweb.common.enums;

/**
 * 定时任务的类型
 */

import com.alibaba.fastjson.annotation.JSONType;
import com.audioweb.common.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 定时任务的类型
 * @ClassName: TaskTimeType 
 * @Description: 定时任务的类型
 * @author 10155 1015510750@qq.com 
 * @date 2020年7月9日 上午9:51:28
 */

@JSONType(serializeEnumAsJavaBean = true)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TaskTimeType{
	DAILY("每日任务"),WEEKLY("每周任务"),MONTHLY("每月任务"),SINGLE("单次任务"),CUSTOM("自定任务");
	 /**
     * 枚举值码
     */
	@JsonValue
    private final String code;
 
	private TaskTimeType(String code) {
		this.code = code;
	}
    public String getCode()
    {
        return code;
    }
    
    public static TaskTimeType invokeEnum(String name) {
    	if(StringUtils.isEmpty(name)) {
    		return null;
    	}
    	TaskTimeType[] types = TaskTimeType.values();
		//遍历查找
	    for(TaskTimeType s : types){
           if(s.name().equals(name) || s.getCode().equals(name)){
               return s;
           }
	    }
		return null;
	}
}
