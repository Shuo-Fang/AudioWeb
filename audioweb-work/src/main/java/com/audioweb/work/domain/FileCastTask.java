/**   
 * @Title: FileCastTask.java 
 * @Package com.audioweb.work.domain 
 * @Description: 文件-定时-文本广播广播对象
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com
 * @date 2020年3月18日 下午4:00:17 
 * @version V1.0   
 */ 
package com.audioweb.work.domain;

import java.io.BufferedInputStream;
import java.util.List;
import java.util.Timer;

import com.audioweb.common.enums.CastWorkType;
import com.audioweb.common.enums.FileCastType;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.server.GroupNettyServer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/** 文件-定时-文本广播广播对象
 * @ClassName: FileCastTask 
 * @Description: 文件广播广播对象
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2020年3月18日 下午4:00:17  
 */
@ApiModel("文件广播实体")
@JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL) 
public class FileCastTask extends WorkCastTask{

	private static final long serialVersionUID = 1L;
	
	/**文件广播发起者sessionId*/
	private String sessionId;
	
	/**文件广播的timer定时器*/
	@JsonIgnore
	private Timer timer;
	
	/** 正在广播的文件信息 */
	@ApiModelProperty("正在广播的文件")
	private WorkFile runFile;
	
	/** 需要广播的文件列表 */
	@ApiModelProperty("需要广播的文件列表")
	@JsonIgnore
	private List<WorkFile> castFileList;
	
	/** 初始化时需要广播的文件列表id 逗号分隔 */
	@ApiModelProperty("需要广播的文件列表id")
	private String songData;
	
	/** 文件广播类型 */
	@ApiModelProperty("广播的文件类型：0,顺序播放;1,列表循环;2,随机播放")
	private FileCastType fileCastType;
	
	/** 音频播放位置*/
	@ApiModelProperty("音频播放位置，单位byte")
	private volatile Long fileSign;
	
	/** 定时剩余时长 */
	@ApiModelProperty("定时剩余时长，单位ms")
	private volatile Long timing;
	
	/** 是否音频播放完再停止 */
	@ApiModelProperty("是否音频播放完再停止")
	private Boolean completeClose = false;
	
	/**文件广播中的音频文件分包大小*/
	@JsonIgnore
	private int bitsize;
	
	/**文件广播中每次广播的时间间隔*/
	@JsonIgnore
	private int timesize;
	
	/**文件读取信息流*/
	@JsonIgnore
	private BufferedInputStream in; 
	
	/**组播对象*/
	@JsonIgnore
	private GroupNettyServer server;
	
	public WorkFile getRunFile() {
		return runFile;
	}

	public void setRunFile(WorkFile runFile) {
		this.runFile = runFile;
	}

	public List<WorkFile> getCastFileList() {
		return castFileList;
	}

	public void setCastFileList(List<WorkFile> castFileList) {
		this.castFileList = castFileList;
	}

	public FileCastType getFileCastType() {
		return fileCastType;
	}

	public void setFileCastType(FileCastType fileCastType) {
		this.fileCastType = fileCastType;
	}

	public Long getFileSign() {
		return fileSign;
	}

	public void setFileSign(Long fileSign) {
		this.fileSign = fileSign;
	}

	public Long getTiming() {
		return timing;
	}

	public void setTiming(Long timing) {
		this.timing = timing;
	}

	public Boolean getCompleteClose() {
		return completeClose;
	}

	public void setCompleteClose(Boolean completeClose) {
		this.completeClose = completeClose;
	}

	public int getBitsize() {
		return bitsize;
	}

	public void setBitsize(int bitsize) {
		this.bitsize = bitsize;
	}

	public int getTimesize() {
		return timesize;
	}

	public void setTimesize(int timesize) {
		this.timesize = timesize;
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public BufferedInputStream getIn() {
		return in;
	}

	public void setIn(BufferedInputStream in) {
		this.in = in;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public String getSongData() {
		return songData;
	}

	public void setSongData(String songData) {
		this.songData = songData;
	}

	public GroupNettyServer getServer() {
		return server;
	}

	public void setServer(GroupNettyServer server) {
		this.server = server;
	}

	public static FileCastTask findRunningTask(String sessionId) {
		List<WorkCastTask> wCastTasks = new WorkCastTask().export();
		for(WorkCastTask task:wCastTasks) {
			if(task.getCastType() == CastWorkType.FILE) {
				FileCastTask fTask = (FileCastTask)task;
				if(StringUtils.isNotEmpty(fTask.getSessionId()) && fTask.getSessionId().equals(sessionId)) {
					return fTask;
				}
			}
		}
		return null;
	}
}
