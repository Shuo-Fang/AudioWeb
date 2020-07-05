/**   
 * @Title: CustomDateSerializer.java 
 * @Package com.audioweb.work.domain.serializer 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author ShuoFang 1015510750@qq.com
 * @date 2020年4月15日 下午2:47:18 
 * @version V1.0   
 */ 
package com.audioweb.work.domain.serializer;

import java.io.IOException;

import com.audioweb.work.domain.WorkCastTask;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/** 广播信息序列化过滤器,只输出简略信息
 * @ClassName: CustomDateSerializer 
 * @Description: 广播信息序列化过滤器，只输出简略信息
 * @author ShuoFang 1015510750@qq.com 
 * @date 2020年4月15日 下午2:47:18  
 */
public class WorkCastTaskSerializer extends JsonSerializer<WorkCastTask> {
	
	@Override
	public void serialize(WorkCastTask value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeStartObject();
		gen.writeNumberField("taskId",value.getTaskId());
		gen.writeStringField("taskName", value.getTaskName());
		gen.writeStringField("castType", value.getCastType().getInfo());
		gen.writeNumberField("vol", value.getVol());
		gen.writeNumberField("castLevel", value.getCastLevel());
		gen.writeEndObject();
	}
}
