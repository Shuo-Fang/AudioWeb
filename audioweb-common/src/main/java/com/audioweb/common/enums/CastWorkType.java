package com.audioweb.common.enums;

import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonFormat;

@JSONType(serializeEnumAsJavaBean = true)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CastWorkType{
	FILE("FILE","文件广播"),TIME("TIME","定时广播"),REAL("REAL","实时采播"),CLIENT("CLIENT","终端采播"),
	POINT("POINT","终端点播"),PAGING("PAGING","寻呼话筒"),PLUG("PLUG","控件广播"),WORD("WORD","文本广播");
	 /**
     * 枚举值码
     */
    private final String code;
 
    /**
     * 枚举描述
     */
    private final String message;
	private CastWorkType(String code,String message) {
		this.code = code;
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public String getCode() {
		return code;
	}
}
