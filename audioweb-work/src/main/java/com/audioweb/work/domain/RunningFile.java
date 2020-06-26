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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.annotation.JsonIgnore;

/** 正在广播音频文件信息
 * @ClassName: RunningFile
 * @Description: 正在广播音频文件信息
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2020年4月13日 下午2:38:38  
 */
public class RunningFile extends WorkFile{
	public static final short DATA_LENGTH = 880;//每次数据包发送文件最大长度
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
	
	/**文件广播中按帧读取一次读取多少帧*/
	@JsonIgnore
	private final int frameCount;
	
	/**文件广播中的是否安插空白帧*/
	@JsonIgnore
	private AtomicInteger blankFrame = new AtomicInteger(0);
	
	/**文件广播中每次广播的时间间隔-纳秒精度*/
	@JsonIgnore
	private final long nanoTimeSize;
	
	/**文件广播中是否按帧发送信息*/
	@JsonIgnore
	private final boolean isFrame;
	
	/**文件广播中文件是否销毁,*/
	@JsonIgnore
	private boolean isNotDestory = true;
	
	/**文件读取信息流*/
	@JsonIgnore
	private BufferedInputStream in;
	
	/**文件帧读取信息流*/
	@JsonIgnore
	private Mp3Stream inStream;
	
	/**正在播放文件时间节点*/
	private AtomicLong playSite = new AtomicLong(0);
	
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
		//初始化时序
		long realTimeSize = 1152L*1000*1000*1000/sampleRate;
		int realBitSize = (int) (super.getBitRate()*realTimeSize/1000/1000 >> 3);
		if(realBitSize > DATA_LENGTH) {
			timesize = (DATA_LENGTH << 3)/super.getBitRate();
			bitsize = (int) (super.getBitRate()*timesize >> 3);
			isFrame =  false;
			nanoTimeSize = timesize * 1000 * 1000;
			frameCount = 0;
		}else if(realBitSize <= DATA_LENGTH-4 >> 1) {
			nanoTimeSize = realTimeSize << 1;
			bitsize = realBitSize << 1;
			timesize = (int) (nanoTimeSize/1000/1000);
			isFrame = true;
			frameCount = 2;
		}else {
			nanoTimeSize = realTimeSize;
			bitsize = realBitSize;
			timesize = (int) (nanoTimeSize/1000/1000);
			isFrame = true;
			frameCount = 1;
		}
		//初始化文件读取信息
		FileInputStream inputStream = new FileInputStream(super.getFilePath());
		in  = new BufferedInputStream(inputStream,inputStream.available());
		if(isFrame) {
			inStream = new Mp3Stream(in,super.getMusicLength());
		}else {
			in.skip(super.getStartByte());
			in.mark((int) (super.getMusicLength()-super.getStartByte()));//标记起始字节，用于回滚
		}
	}

	/**重置标记至起始**/
	public final void resetIn() {
		try {
			in.reset();
			if(isFrame) {
				inStream.parserMp3Header();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**正在播放文件发送一次*/
	public final long runStep() {
		return playSite.addAndGet(timesize);
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

	public final long getPlaySite() {
		return playSite.get();
	}

	public final void setPlaySite(long playSite) {
		this.playSite.set(playSite);
	}
	
	public long getNanoTimeSize() {
		return nanoTimeSize;
	}

	public boolean isFrame() {
		return isFrame;
	}

	public Mp3Stream getInStream() {
		return inStream;
	}

	public int getFrameCount() {
		return frameCount;
	}
	
	public int getBlankFrame() {
		return blankFrame.get();
	}
	public int addBlankFrame() {
		return blankFrame.incrementAndGet();
	}

	public void setBlankFrame(int blankFrame) {
		this.blankFrame.set(blankFrame);
	}

	/**加载跳过文件进度
	 * @throws IOException */
	public final void loadPlaySite(long playSite) throws IOException {
		in.skip(playSite);
	}
}
