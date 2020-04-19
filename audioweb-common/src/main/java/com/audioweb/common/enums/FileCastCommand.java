package com.audioweb.common.enums;

import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JSONType(serializeEnumAsJavaBean = true)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FileCastCommand {
	PREV("上一曲"),PAUSE("暂停"),RESUME("继续"),NEXT("下一曲");
	 /**
    * 枚举值码
    */
	@JsonValue
   private final String code;

	private FileCastCommand(String code) {
		this.code = code;
	}
   public String getCode()
   {
       return code;
   }
}
