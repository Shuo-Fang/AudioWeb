/**   
 * @Title: FileCastTask.java 
 * @Package com.audioweb.work.domain 
 * @Description: 文件-定时-文本广播广播对象
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com
 * @date 2020年3月18日 下午4:00:17 
 * @version V1.0   
 */ 
package com.audioweb.work.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

import com.audioweb.common.core.text.Convert;
import com.audioweb.common.enums.CastWorkType;
import com.audioweb.common.enums.FileCastType;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.server.GroupNettyServer;
import com.audioweb.server.service.TimeFileCast;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;

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
	private TimeFileCast timer;
	
	/** 正在广播的文件信息 */
	@ApiModelProperty("正在广播的文件")
	private RunningFile runFile;
	
	/** 需要广播的文件列表 */
	@ApiModelProperty("需要广播的文件列表")
	@JsonIgnore
	private List<WorkFile> castFileList = new LinkedList<WorkFile>();;
	
	/** 初始化时需要广播的文件列表id 逗号分隔 */
	@ApiModelProperty("需要广播的文件列表id")
	private List<String> songData;
	
	/** 广播文件历史列表 */
	@ApiModelProperty("广播文件历史列表")
	private List<String> playHistory = new LinkedList<String>();
	
	/** 文件广播类型 */
	@ApiModelProperty("广播的文件类型：0,顺序播放;1,列表循环;2,随机播放")
	private FileCastType fileCastType;
	
	/** 音频播放位置*/
/*	@ApiModelProperty("音频播放位置，单位byte")
	private volatile Long fileSign;*/
	
	/** 定时剩余时长 */
	@ApiModelProperty("定时剩余时长，单位ms")
	private volatile Long timing;
	
	/** 是否音频播放完再停止 */
	@ApiModelProperty("是否音频播放完再停止")
	private Boolean completeClose = false;
	
	/**组播对象*/
	@JsonIgnore
	private GroupNettyServer server;
	
	public RunningFile getRunFile() {
		return runFile;
	}

	public void setRunFile(RunningFile runFile) {
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

	public Long getTiming() {
		return timing;
	}

	public void setTiming(Long timing) {
		this.timing = timing;
	}
	
	/**文件广播倒计时播放调用步进*/
	public void stepTiming() {
		timing -= runFile.getTimesize();
		if(timing <= 0) {
			timing = 0L;
		}
	}

	public Boolean getCompleteClose() {
		return completeClose;
	}

	public void setCompleteClose(Boolean completeClose) {
		this.completeClose = completeClose;
	}
	
	public TimeFileCast getTimer() {
		return timer;
	}

	public void setTimer(TimeFileCast timer) {
		this.timer = timer;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	/**获取字符串格式的音频播放列表*/
	@JsonGetter("songData")
	public String getSongData() {
		return Convert.listToStr(songData);
	}
	
	public List<String> findSongDataList() {
		return songData;
	}
	
	/**字符串格式的音频播放列表存储为list*/
	@JsonSetter("songData")
	public void setSongData(String songData) {
		this.songData = new LinkedList<>(Convert.strToList(songData));
	}
	
	public void putSongDataList(List<String> songDataList) {
		this.songData = songDataList;
	}

	public GroupNettyServer getServer() {
		return server;
	}

	public void setServer(GroupNettyServer server) {
		this.server = server;
	}
	
	/**获取字符串格式的历史音频播放列表*/
	@JsonGetter("playHistory")
	public String getPlayHistory() {
		return Convert.listToStr(playHistory);
	}

	public List<String> findPlayHistoryList() {
		return playHistory;
	}
	
	/**字符串格式的历史音频播放列表存储为list*/
	@JsonSetter("playHistory")
	public void setPlayHistory(String playHistory) {
		this.playHistory = new LinkedList<>(Convert.strToList(playHistory));
	}

	public void putPlayHistoryList(List<String> playHistory) {
		this.playHistory = playHistory;
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
