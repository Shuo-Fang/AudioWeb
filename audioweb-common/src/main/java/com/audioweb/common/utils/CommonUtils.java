/**   
 * @Title: CommonUtils.java 
 * @Package com.audioweb.common.utils 
 * @Description: 通用工具类
 * @author ShuoFang 1015510750@qq.com
 * @date 2020年3月11日 下午1:08:20 
 * @version V1.0   
 */ 
package com.audioweb.common.utils;

import java.util.LinkedHashSet;
import java.util.List;

/** 通用工具类
 * @ClassName: CommonUtils 
 * @Description: 通用工具类
 * @author ShuoFang 1015510750@qq.com 
 * @date 2020年3月11日 下午1:08:20  
 */
public class CommonUtils {
	/**
     * list去重复元素
     * @Title: removeDuplicate 
     * @param @param list
     * @return void
     */
    @SuppressWarnings("unchecked")
	public static void removeDuplicate(@SuppressWarnings("rawtypes") List list) {
   	    LinkedHashSet<Object> set = new LinkedHashSet<Object>(list.size());
   	    set.addAll(list);
   	    list.clear();
   	    list.addAll(set);
   	}
}
