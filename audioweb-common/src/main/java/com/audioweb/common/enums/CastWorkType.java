package com.audioweb.common.enums;

import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonFormat;

@JSONType(serializeEnumAsJavaBean = true)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CastWorkType {
	FILE(0,"文件广播"),TIME(1,"定时广播"),REAL(2,"实时采播"),CLIENT(3,"终端采播"),
	POINT(4,"终端点播"),PAGING(5,"寻呼话筒"),PLUG(6,"控件广播"),WORD(7,"文本广播");
	 /**
     * 枚举值码
     */
    private final Integer code;
 
    /**
     * 枚举描述
     */
    private final String message;
	private CastWorkType(Integer code,String message) {
		this.code = code;
		this.message = message;
	}
    public Integer getCode()
    {
        return code;
    }
	public String getMessage() {
		return message;
	}
}
