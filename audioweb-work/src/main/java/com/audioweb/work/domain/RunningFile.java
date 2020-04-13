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
import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.annotation.JsonIgnore;

/** 正在广播音频文件信息
 * @ClassName: RunningFile
 * @Description: 正在广播音频文件信息
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2020年4月13日 下午2:38:38  
 */
public class RunningFile extends WorkFile{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**文件广播中的音频文件分包大小*/
	@JsonIgnore
	private int bitsize;
	
	/**文件广播中每次广播的时间间隔*/
	@JsonIgnore
	private int timesize;
	
	/**文件读取信息流*/
	@JsonIgnore
	private BufferedInputStream in;
	
	/**正在播放文件时间节点*/
	private AtomicLong palySite = new AtomicLong(0);
	
	private RunningFile() {}
	
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

	public BufferedInputStream getIn() {
		return in;
	}

	public void setIn(BufferedInputStream in) {
		this.in = in;
	}

	public long getPalySite() {
		return palySite.get();
	}

	public void setPalySite(long palySite) {
		this.palySite.set(palySite);
	}
	
	/**正在播放文件发送一次*/
	public long runStep() {
		return this.palySite.addAndGet(timesize);
	}
	
	

	/**克隆获取新的正在广播音频文件信息*/
	public static RunningFile getRunningFile(WorkFile file) throws CloneNotSupportedException {
		RunningFile runningFile = (RunningFile) file.clone();
		return runningFile;
	}

}
