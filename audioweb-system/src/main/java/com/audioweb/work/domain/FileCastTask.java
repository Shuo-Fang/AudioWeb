/**   
 * @Title: FileCastTask.java 
 * @Package com.audioweb.work.domain 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com
 * @date 2020年3月18日 下午4:00:17 
 * @version V1.0   
 */ 
package com.audioweb.work.domain;

import java.util.List;

/** 文件-定时广播广播对象
 * @ClassName: FileCastTask 
 * @Description: 文件广播广播对象
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2020年3月18日 下午4:00:17  
 */
public class FileCastTask extends CastTask{

	private static final long serialVersionUID = 1L;
	
	/** 正在广播的文件信息 */
	private WorkFile runFile;
	
	/** 需要广播的文件列表 */
	private List<WorkFile> castFileList;
	
	/** 随机广播20位长度序列号*/
	private String randomSerial;
	
	/** 文件广播类型*/
	private String fileCastType;
	
	/** 音频播放位置*/
	private volatile Long fileSign;
	
}
