package com.audioweb.common.enums;

import com.alibaba.fastjson.annotation.JSONType;
import com.audioweb.common.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JSONType(serializeEnumAsJavaBean = true)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CastWorkType{
	FILE("文件广播"),TIME("定时广播"),REAL("实时采播"),PLUG("控件广播"),
	WORD("文本广播"),POINT("终端点播"),CLIENT("终端采播"),PAGING("寻呼话筒");
	 /**
     * 枚举值码
     */
    private final String info;
	private CastWorkType(String info) {
		this.info = info;
	}
	@JsonValue
	public String getInfo() {
		return info;
	}
	
	public static CastWorkType invokeEnum(String name) {
		if(StringUtils.isEmpty(name)) {
    		return null;
    	}
		CastWorkType[] types = CastWorkType.values();
		//遍历查找
	    for(CastWorkType s : types){
           if(s.name().equals(name) || s.getInfo().equals(name)){
               return s;
           }
	    }
		return null;
	}
}
