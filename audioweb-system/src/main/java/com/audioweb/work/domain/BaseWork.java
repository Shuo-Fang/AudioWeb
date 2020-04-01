package com.audioweb.work.domain;

import java.util.List;

import com.audioweb.common.core.domain.BaseEntity;
/** 缓存方法接口 */
public interface BaseWork {
	
	/**将本对象放入缓存中*/
	public boolean put();
	
	/**判断本对象在缓存中是否存在*/
	public boolean exist();
	
	/**从缓存中获取存储的本对象*/
	public BaseEntity get();
	
	/**从缓存中删除存储的本对象*/
	public boolean remove();
	
	/**清除全部的缓存*/
	public void clear();
	
	/**将全部缓存导出*/
	public List<?> export();
	
}
