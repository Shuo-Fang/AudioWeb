/**   
 * @Title: FileCastTask.java 
 * @Package com.audioweb.work.domain 
 * @Description: 文件-定时-文本广播广播对象
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com
 * @date 2020年3月18日 下午4:00:17 
 * @version V1.0   
 */ 
package com.audioweb.work.domain;

import java.util.List;
import java.util.Timer;

import com.audioweb.common.enums.FileCastType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/** 文件-定时-文本广播广播对象
 * @ClassName: FileCastTask 
 * @Description: 文件广播广播对象
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2020年3月18日 下午4:00:17  
 */
@ApiModel("文件广播实体")
public class FileCastTask extends WorkCastTask{

	private static final long serialVersionUID = 1L;
	
	/**文件广播的timer定时器*/
	transient  private Timer timer;
	
	/** 正在广播的文件信息 */
	@ApiModelProperty("正在广播的文件")
	private WorkFile runFile;
	
	/** 需要广播的文件列表 */
	@ApiModelProperty("需要广播的文件列表")
	private List<WorkFile> castFileList;
	
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
	private Boolean completeClose;
	
	/**文件广播中的音频文件分包大小*/
	transient private int bitsize;
	
	/**文件广播中每次广播的时间间隔*/
	transient private int timesize;
	
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
}
