package com.audioweb.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**   
 * @Title: TestMain.java 
 * @Package  
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com
 * @date 2020年4月16日 上午11:41:08 
 * @version V1.0   
 */

/** 
 * @ClassName: TestMain 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2020年4月16日 上午11:41:08  
 */
public class TestMain {
	public static void main(String[] args) {
		String[] str = new String[] { "yang", "hao" };
		 
		List<String> list = new LinkedList<>(Arrays.asList(str));

		list.add("test");
		method(null);
	}
		 
		public static void method(String param) {
		 
		switch (param) {
		 
		// 肯定不是进入这里
		 
		case "sth":
		 
		System.out.println("it's sth");
		 
		break;
		 
		// 也不是进入这里
		 
		case "null":
		 
		System.out.println("it's null");
		 
		break;
		 
		// 也不是进入这里
		 
		default:
		 
		System.out.println("default");
		 
		}
		 
		}
}
