package com.audioweb.work.domain;

import java.util.List;

import com.audioweb.common.core.domain.BaseEntity;
/** 缓存方法接口 */
public interface BaseRunning {
	
	public boolean put();
	
	public boolean exist();
	
	public BaseEntity get();
	
	public void clear();
	
	public List<?> getList();
	
}
