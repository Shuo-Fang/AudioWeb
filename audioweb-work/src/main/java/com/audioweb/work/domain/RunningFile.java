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
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

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
	private final int  bitsize;
	
	/**文件广播中每次广播的时间间隔*/
	@JsonIgnore
	private final int timesize;
	
	/**文件广播中文件是否销毁,*/
	@JsonIgnore
	private boolean isNotDestory = true;
	
	/**文件读取信息流*/
	@JsonIgnore
	private final BufferedInputStream in;
	
	/**正在播放文件时间节点*/
	private AtomicLong palySite = new AtomicLong(0);
	
	/**初始化继承父类信息
	 * @throws IOException */
	private RunningFile(WorkFile file) throws IOException {
		album = file.getAlbum();
		artist = file.getArtist();
		bitRate = file.getBitRate();
		delFlag = file.getDelFlag();
		duration = file.getDuration();
		fileId = file.getFileId();
		fileName = file.getFileName();
		filePath = file.getFilePath();
		fileType = file.getFileType();
		format = file.getFormat();
		imagePath = file.getImagePath();
		imageVirPath = file.getImageVirPath();
		musicLength = file.getMusicLength();
		sampleRate = file.getSampleRate();
		songName = file.getSongName();
		startByte = file.getStartByte();
		virPath = file.getVirPath();
		timesize = (DATA_LENGTH*8)/super.getBitRate();
		bitsize = super.getBitRate()*timesize/8;
		//初始化文件读取信息
		FileInputStream inputStream = new FileInputStream(super.getFilePath());
		in  = new BufferedInputStream(inputStream,inputStream.available());
		in.skip(super.getStartByte());
		in.mark((int) (super.getMusicLength()-super.getStartByte()));//标记起始字节，用于回滚
	}

	/**重置标记至起始**/
	public final void resetIn() {
		try {
			in.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**正在播放文件发送一次*/
	public final long runStep() {
		return palySite.addAndGet(timesize);
	}

	/**获取新的正在广播音频文件信息
	 * @throws IOException */
	public static RunningFile getRunningFile(WorkFile file) throws IOException{
		RunningFile runningFile = null;
		runningFile = new RunningFile(file);
		return runningFile;
	}
	
	public final void destory() {
		try {
			isNotDestory = false;
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	
	public final boolean isNotDestory() {
		return isNotDestory;
	}

	public final long getPalySite() {
		return palySite.get();
	}

	public final void setPalySite(long palySite) {
		this.palySite.set(palySite);
	}
}
