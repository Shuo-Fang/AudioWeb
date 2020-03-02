/**   
 * @Title: CastTask.java 
 * @Package com.audioweb.system.domain 
 * @Description: 广播任务信息
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com
 * @date 2020年3月2日 下午1:27:26 
 * @version V1.0   
 */ 
package com.audioweb.work.domain;

import java.util.List;

/** 
 * @ClassName: CastTask 
 * @Description: 广播任务信息
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2020年3月2日 下午1:27:26  
 */
public class CastTask {
	private static final long serialVersionUID = 1L;
	/** 广播编号ID */
	private long taskId;
	
	/** 广播任务名称 */
	private String taskName;
	
	/** 广播音量 */
	private int vol = -1;
	
	/**	广播类型 */
	private String castType;
	
	/**	广播地址 */
	private String castAddress;
	
	/**	广播端口 */
	private int castPort;
	
	/**	广播级别 */
	private int castLevel;
	
	/**	广播初始化分区列表 */
	private List<String> domainidlist;//广播分区列表
	
	/**	广播初始化终端列表 */
	private List<WorkTerminal> castTeridlist;//广播终端列表

	/**	正在广播终端列表 */
	private List<WorkTerminal> castlist;//正在广播终端列表
	
	/** 正在播放音频信息*/
	//private RunFile runfile;
}
