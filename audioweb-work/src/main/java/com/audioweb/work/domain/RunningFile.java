/**   
 * @Title: RunningFile.java 
 * @Package com.audioweb.work.domain 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com
 * @date 2020年4月13日 下午2:38:38 
 * @version V1.0   
 */ 
package com.audioweb.work.domain;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import com.audioweb.common.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

/** 正在广播音频文件信息
 * @ClassName: RunningFile
 * @Description: 正在广播音频文件信息
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2020年4月13日 下午2:38:38  
 */
public class RunningFile extends WorkFile{
	public static final short DATA_LENGTH = 800;//每次数据包发送文件最大长度
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**文件广播中的音频文件分包大小*/
	@JsonIgnore
	private int  bitsize;
	
	/**文件广播中每次广播的时间间隔*/
	@JsonIgnore
	private int timesize;
	
	/**文件读取信息流*/
	@JsonIgnore
	private BufferedInputStream in;
	
	/**正在播放文件时间节点*/
	private AtomicLong palySite = new AtomicLong(0);
	
	/**初始化继承父类信息*/
	private RunningFile(WorkFile file) {
		this.setAlbum(file.getAlbum());
		this.setArtist(file.getArtist());
		this.setBitRate(file.getBitRate());
		this.setCreateBy(file.getCreateBy());
		this.setCreateTime(file.getCreateTime());
		this.setDelFlag(file.getDelFlag());
		this.setDuration(file.getDuration());
		this.setFileId(file.getFileId());
		this.setFileName(file.getFileName());
		this.setFilePath(file.getFilePath());
		this.setFileType(file.getFileType());
		this.setFormat(file.getFormat());
		this.setImagePath(file.getImagePath());
		this.setImageVirPath(file.getImageVirPath());
		this.setMusicLength(file.getMusicLength());
		this.setRemark(file.getRemark());
		this.setSampleRate(file.getSampleRate());
		this.setSongName(file.getSongName());
		this.setStartByte(file.getStartByte());
		this.setUpdateBy(file.getUpdateBy());
		this.setUpdateTime(file.getUpdateTime());
		this.setVirPath(file.getVirPath());
	}
	
	public final int getBitsize() {
		return bitsize;
	}

	public final int getTimesize() {
		return timesize;
	}

	public final BufferedInputStream getIn() {
		return in;
	}

	public final long getPalySite() {
		return palySite.get();
	}

	public final void setPalySite(long palySite) {
		this.palySite.set(palySite);
	}
	
	/**正在播放文件发送一次*/
	public final long runStep() {
		return this.palySite.addAndGet(timesize);
	}
	
	/**初始化文件读取信息*/
	public void initBufferedInputStream() throws IOException {
		timesize = (DATA_LENGTH*8)/super.getBitRate();
		bitsize = super.getBitRate()*timesize/8;
		FileInputStream file = new FileInputStream(super.getFilePath());
		synchronized (in != null? in:this) {
			in  = new BufferedInputStream(file,file.available());
			in.skip(super.getStartByte());
		}
	}

	/**克隆获取新的正在广播音频文件信息
	 * @throws IOException */
	public static RunningFile getRunningFile(WorkFile file) throws IOException{
		RunningFile runningFile = null;
		runningFile = new RunningFile(file);
		runningFile.initBufferedInputStream();
		return runningFile;
	}

}
