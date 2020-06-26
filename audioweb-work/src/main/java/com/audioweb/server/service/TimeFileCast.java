/**   
 * @Title: TimeFileCast.java 
 * @Package com.audioweb.server.service 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com
 * @date 2020年4月13日 下午5:24:39 
 * @version V1.0   
 */ 
package com.audioweb.server.service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.audioweb.common.enums.ClientCommand;
import com.audioweb.common.thread.manager.AsyncManager;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.server.protocol.InterCmdProcess;
import com.audioweb.work.domain.FileCastTask;
import com.audioweb.work.domain.RunningFile;

/** 
 * @ClassName: TimeFileCast 
 * @Description: 文件广播定时器信息
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2020年4月13日 下午5:24:39  
 */
public class TimeFileCast extends TimerTask{
	/**定时器身份证明*/
	private ScheduledFuture<?> castCard;
	
	/**定时器关联任务*/
	private FileCastTask task;
	
	public TimeFileCast(FileCastTask task) {
		this.task = task;
		if(task.getRunFile().isFrame()) {
			castCard = AsyncManager.me().scheduleExecute(this,task.getRunFile().getNanoTimeSize(),
					task.getRunFile().getNanoTimeSize(),TimeUnit.NANOSECONDS);
		}else {
			castCard = AsyncManager.me().scheduleExecute(this,task.getRunFile().getTimesize(),
					task.getRunFile().getTimesize(),TimeUnit.MILLISECONDS);
		}
	}
	
	@Override
	public void run() {
		if(task.getRunFile().isFrame()) {
			ByteBuffer dataBuffer = ByteBuffer.allocate(1024);
			try {
				if(task.getIsCast()) {//是否在广播
					if(!(task.getIsStop() || task.getRunFile().getBlankFrame() != 0)) {//是否在暂停或为为空白帧
						if (StringUtils.isNull(task.getTiming())) {//无倒计时
							if (StringUtils.isNotNull(task.getServer().getChannel()) && task.getServer().getChannel().isWritable()) {// 是否关闭
								int read = read(dataBuffer);//读取音频数据
								send(dataBuffer);// 继续发送音频
								if (read == -1) {//文件已读完
									WorkFileTaskService.initFileRead(task);
								}
							} else {
								//停止广播
								destory();
							}
						} else{//有倒计时
							if (task.getTiming() > 0) {// 时间未用完
								int read = read(dataBuffer);
								task.stepTiming();//将倒计时时间减少
								send(dataBuffer);// 继续发送音频
								if (read == -1) {//文件已读完
									WorkFileTaskService.initFileRead(task);
								}
							} else if(task.getTiming() <= 0 && task.getCompleteClose()){//是否播放完再停止
								int read = read(dataBuffer);//读取音频数据
								send(dataBuffer);// 继续发送音频
								if (read == -1) {//文件已读完
									destory();// 关闭广播
								}
							} else {
								destory();// 时间用完立即关闭
							}
						}
					}else {
						if (StringUtils.isNotNull(task.getServer().getChannel()) && task.getServer().getChannel().isWritable()) {
							dataBuffer.put(new byte[task.getRunFile().getBitsize()]);
							send(dataBuffer);// 发送空文件，保持终端播放状态
							if(task.getRunFile().getBlankFrame() > 2) {
								task.getRunFile().setBlankFrame(0);
							}else {
								task.getRunFile().addBlankFrame();
							}
						} else {
							//停止广播
							destory();
						}
					}
				}else {
					destory();
				}
			}catch (Exception e) {
				task.setIsCast(false);
				e.printStackTrace();
			}
		}else {
			byte[] data = new byte[task.getRunFile().getBitsize()+ClientCommand.CMD_HEADER_SIZE.getCmd()];//加上16位数据用作数据头
			try {
				if(task.getIsCast()) {//是否在广播
					if(!(task.getIsStop() || task.getRunFile().getBlankFrame() != 0)) {//是否在暂停或为为空白帧
						if (StringUtils.isNull(task.getTiming())) {//无倒计时
							if (StringUtils.isNotNull(task.getServer().getChannel()) && task.getServer().getChannel().isWritable()) {// 是否关闭
								int read = read(data);//读取音频数据
								send(data);// 继续发送音频
								if (read == -1) {//文件已读完
									WorkFileTaskService.initFileRead(task);
								}
							} else {
								//停止广播
								destory();
							}
						} else{//有倒计时
							if (task.getTiming() > 0) {// 时间未用完
								int read = read(data);
								task.stepTiming();//将倒计时时间减少
								send(data);// 继续发送音频
								if (read == -1) {//文件已读完
									WorkFileTaskService.initFileRead(task);
								}
							} else if(task.getTiming() <= 0 && task.getCompleteClose()){//是否播放完再停止
								int read = read(data);//读取音频数据
								send(data);// 继续发送音频
								if (read == -1) {//文件已读完
									destory();// 关闭广播
								}
							} else {
								destory();// 时间用完立即关闭
							}
						}
					}else {
						if (StringUtils.isNotNull(task.getServer().getChannel()) && task.getServer().getChannel().isWritable()) {
							send(data);// 发送空文件，保持终端播放状态
							if(task.getRunFile().getBlankFrame() > 2) {
								task.getRunFile().setBlankFrame(0);
							}else {
								task.getRunFile().addBlankFrame();
							}
						} else {
							//停止广播
							destory();
						}
					}
				}else {
					destory();
				}
			}catch (Exception e) {
				task.setIsCast(false);
				e.printStackTrace();
			}
		}
	}
	/**重置定时器*/
	public void reloadTimer() {
		final ScheduledFuture<?> oldCastCard = castCard;
		if (oldCastCard != null) {
			oldCastCard.cancel(false);
	    }
		if(task.getRunFile().isFrame()) {
			castCard = AsyncManager.me().scheduleExecute(this,task.getRunFile().getNanoTimeSize(),
					task.getRunFile().getNanoTimeSize(),TimeUnit.NANOSECONDS);
		}else {
			castCard = AsyncManager.me().scheduleExecute(this,task.getRunFile().getTimesize(),
					task.getRunFile().getTimesize(),TimeUnit.MILLISECONDS);
		}
	}
	/**
	 * 结束广播
	 */
	private void destory() {
		/**广播结束**/
		ByteBuffer endbs = InterCmdProcess.sendCast(false,"", 0,0,task.getCastType());
		try {
			task.getServer().sendData(endbs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		/**停止定时器**/
		if (castCard != null) {
			castCard.cancel(false);
	    }
		AsyncManager.me().execute(new TimerTask() {
			@Override
			public void run() {
				WorkServerService.closeTask(task);//停止任务
			}
		});
	}
	/**
	 * 结束广播
	 */
	public void destoryTimer() {
		/**停止定时器**/
		if (castCard != null) {
			castCard.cancel(false);
		}
		/**广播结束**/
		ByteBuffer endbs = InterCmdProcess.sendCast(false,"", 0,0,task.getCastType());
		try {
			task.getServer().sendData(endbs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**发送音频信息*/
	private void send(byte[] data) {
		InterCmdProcess.sendDataPackt(data);
		task.getServer().sendData(data);
	}
	/**发送音频信息*/
	private void send(ByteBuffer data) {
		InterCmdProcess.sendDataPackt(data);
		task.getServer().sendData(data);
	}
	/**读取音频信息**/
	private int read(byte[] data) throws IOException {
		int read = -1;
		task.lock.lock();
		try {
			if(task.getRunFile().isNotDestory()) {
				read = task.getRunFile().getIn().read(data,ClientCommand.CMD_HEADER_SIZE.getCmd(),task.getRunFile().getBitsize());
				task.getRunFile().runStep();//将文件发送时长推进一步
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			task.lock.unlock();
		}
		return read;
	}
	/**读取音频信息**/
	private int read(ByteBuffer data) throws IOException {
		int read = -1;
		task.lock.lock();
		try {
			byte[] padding = new byte[ClientCommand.CMD_HEADER_SIZE.getCmd()];
			data.put(padding);
			if(task.getRunFile().isNotDestory()) {
				read = task.getRunFile().getInStream().ParserFrame(data, task.getRunFile().getFrameCount());
				task.getRunFile().runStep();//将文件发送时长推进一步
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			task.lock.unlock();
		}
		return read;
	}
}
