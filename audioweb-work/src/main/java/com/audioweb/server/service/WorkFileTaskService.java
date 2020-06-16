/**
 * @Title: WorkFileTaskService.java
 * @Package com.audioweb.server.service
 * @Description: 广播中文件管理静态方法
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com
 * @date 2020年4月13日 下午1:50:18
 * @version V1.0
 */
package com.audioweb.server.service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.Random;

import com.audioweb.common.utils.StringUtils;
import com.audioweb.server.protocol.InterCmdProcess;
import com.audioweb.work.domain.FileCastTask;
import com.audioweb.work.domain.RunningFile;

/** 广播中文件管理静态方法
 * @ClassName: WorkFileTaskService
 * @Description: 广播中文件管理静态方法
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com
 * @date 2020年4月13日 下午1:50:18
 */
public class WorkFileTaskService {
	/**初始化正在播放文件信息*/
	public static boolean initFileRead(FileCastTask task) {
		try {
			/**若正在播放文件不为空*/
			if(StringUtils.isNotNull(task.getRunFile())) {
				if(task.findSongDataList().size() <= 0) {
					/**音频列表为空*/
					//停止广播
					WorkServerService.closeTask(task);
					return false;
				}
				/**表示为自动播放完成后结束调用，先判断播放类型*/
				switch (task.getFileCastType()) {
					//顺序播放
					case ORDER:
						//判断正在播放音频是否为最后一个音频
						synchronized (task.findSongDataList()) {
							if(Objects.equals(task.getRunFile().getFileId(), task.findSongDataList().get(task.findSongDataList().size()-1))) {
								//停止广播
								WorkServerService.closeTask(task);
							}else {
								//下一曲并继续播放
								int step = task.findSongDataList().indexOf(task.getRunFile().getFileId());
								step += 1;
								if(!fileRead(task,step)) {
									/**文件读取有误**/
									task.removeWorkFile(step);
									return initFileRead(task);
								}
							}
						}
						break;
					//列表循环
					case LIST:
						//下一曲并继续播放
						synchronized (task.findSongDataList()) {
							int step = task.findSongDataList().indexOf(task.getRunFile().getFileId());
							step += 1;
							if(step >= task.findSongDataList().size()-1) {
								step = 0;
							}
							if(!fileRead(task,step)) {
								/**文件读取有误**/
								task.removeWorkFile(step);
								return initFileRead(task);
							}
						}
						break;
					//单曲循环
					case SINGLE:
						//重复一曲
						task.lock.lock();
						try {
							task.getRunFile().resetIn();
							task.getRunFile().setPlaySite(0);
						} catch (Exception e) {
							throw e;
						}finally {
							task.lock.unlock();
						}
						break;
					//随机播放
					case RANDOM:
						//下一曲并继续播放
						synchronized (task.findSongDataList()) {
							if(task.getPlayHistorySite() < task.getPlayHistory().size()) {
								/**播放历史记录*/
								String fileId = task.getPlayHistory().get(task.nextPlayHistorySite());
								if(fileId.equals(task.getRunFile().getFileId())) {
									/**重新遍历调取**/
									return nextFile(task,true);
								}
								int step = task.findSongDataList().indexOf(fileId);
								if(step > 0) {
									if(!fileRead(task,step)) {
										/**文件读取有误**/
										task.removeWorkFile(step);
										return initFileRead(task);
									}
								}else {
									/**文件已从列表删除*/
									/**重新遍历调取**/
									nextFile(task,true);
								}
							}else {
								/**随机播放*/
								int step = 0;
								//列表文件数大于1，才有随机的必要
								if(task.findSongDataList().size() > 1) {
									int location = task.findSongDataList().indexOf(task.getRunFile().getFileId());
									step = task.getRandom().nextInt(task.findSongDataList().size());
									//去除重复播放
									while(location == step) {
										step = task.getRandom().nextInt(task.findSongDataList().size());
									}
								}
								/**保存历史记录*/
								task.putPlayHistory(task.getRunFile().getFileId());
								if(task.getPlayHistorySite() < 100) {
									task.nextPlayHistorySite();
								}
								if(!fileRead(task,step)) {
									/**文件读取有误**/
									task.removeWorkFile(step);
									return initFileRead(task);
								}
							}
						}
						break;
					default:
						//数据有误，停止播放
						WorkServerService.closeTask(task);
						break;
				}
			}else{
				RunningFile file = RunningFile.getRunningFile(task.getCastFileList().get(0));
				task.setRunFile(file);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		task.setIsCast(false);
		return false;
	}

    /***
     * 是否调节下一曲
     * @Title: NextFile
     * @Description: 是否调节下一曲
     * @param task 广播任务
     * @param isNext 是则下一曲，否则上一曲
     * @return boolean 返回类型
     * @throws
     * @author 10155
     * @date 2020年4月20日 上午12:01:30
     */
    public static boolean nextFile(FileCastTask task,boolean isNext) {
    	try {
    		if(task.findSongDataList().size() <= 0) {
				/**音频列表为空*/
				//停止广播
				WorkServerService.closeTask(task);
				return false;
			}
	    	/**先判断播放类型*/
			switch (task.getFileCastType()) {
				/**顺序播放 单曲循环 列表循环**/
				case ORDER:
				case SINGLE:
				case LIST:
					if(isNext) {
						//下一曲并继续播放
						synchronized (task.findSongDataList()) {
							int step = task.findSongDataList().indexOf(task.getRunFile().getFileId());
							step += 1;
							if(step > task.findSongDataList().size()-1) {
								step = 0;
							}
							if(!fileRead(task,step)) {
								/**文件读取有误**/
								task.removeWorkFile(step);
								return nextFile(task, true);
							}
						}
					}else {
						//上一曲并继续播放
						synchronized (task.findSongDataList()) {
							int step = task.findSongDataList().indexOf(task.getRunFile().getFileId());
							step -= 1;
							if(step < 0) {
								step = task.findSongDataList().size()-1;
							}
							if(!fileRead(task,step)) {
								/**文件读取有误**/
								task.removeWorkFile(step);
								return nextFile(task, false);
							}
						}
					}
					break;
				//随机播放
				case RANDOM:
					if(isNext) {
						//下一曲并继续播放
						synchronized (task.findSongDataList()) {
							if(task.getPlayHistorySite() < task.getPlayHistory().size()) {
								/**播放历史记录*/
								String fileId = task.getPlayHistory().get(task.nextPlayHistorySite());
								if(fileId.equals(task.getRunFile().getFileId())) {
									/**重新遍历调取**/
									return nextFile(task, true);
								}
								int step = task.findSongDataList().indexOf(fileId);
								if(step >= 0) {
									if(!fileRead(task,step)) {
										/**文件读取有误**/
										task.removeWorkFile(step);
										return nextFile(task, true);
									}
								}else {
									/**文件已从列表删除*/
									/**重新遍历调取**/
									nextFile(task, true);
								}
							}else {
								/**随机播放*/
								int size = task.findSongDataList().size();
								int step = 0;
								//列表文件数大于1，才有随机的必要
								if(size > 1) {
									int location = task.findSongDataList().indexOf(task.getRunFile().getFileId());
									step = task.getRandom().nextInt(task.findSongDataList().size());
									//去除重复播放
									while(location == step) {
										step = task.getRandom().nextInt(task.findSongDataList().size());
									}
								}
								/**保存历史记录*/
								task.putPlayHistory(task.getRunFile().getFileId());
								if(task.getPlayHistorySite() < 100) {
									task.nextPlayHistorySite();
								}
								if(!fileRead(task,step)) {
									/**文件读取有误**/
									task.removeWorkFile(step);
									return nextFile(task, true);
								}
							}
						}
					}else {
						//上一曲并继续播放
						synchronized (task.findSongDataList()) {
							if(task.getPlayHistorySite() == task.getPlayHistory().size()) {
								/**为边际的上一曲*/
								task.putPlayHistory(task.getRunFile().getFileId());
								if(task.getPlayHistorySite() >= 100) {
									task.prevPlayHistorySite();
								}
							}
							task.prevPlayHistorySite();
							if(task.getPlayHistorySite() < 0) {
								/**随机播放*/
								Random random = new Random();
								int size = task.findSongDataList().size();
								int step = 0;
								//列表文件数大于1，才有随机的必要
								if(size > 1) {
									int location = task.findSongDataList().indexOf(task.getRunFile().getFileId());
									step = random.nextInt(task.findSongDataList().size());
									//去除重复播放
									while(location == step) {
										step = random.nextInt(task.findSongDataList().size());
									}
								}
								/**保存历史记录*/
								if(!task.findSongDataList().get(step).equals(task.getRunFile().getFileId())) {
									task.putPrevPlayHistory(task.getRunFile().getFileId());
								}
								task.setPlayHistorySite(0);
								if(!fileRead(task,step)) {
									/**文件读取有误**/
									task.removeWorkFile(step);
									return nextFile(task, false);
								}
							}else {
								/**播放历史记录*/
								String fileId = task.getPlayHistory().get(task.getPlayHistorySite());
								if(fileId.equals(task.getRunFile().getFileId())) {
									/**重新遍历调取**/
									return nextFile(task, false);
								}
								int step = task.findSongDataList().indexOf(fileId);
								if(step >= 0) {
									if(!fileRead(task,step)) {
										/**文件读取有误**/
										task.removeWorkFile(step);
										return nextFile(task, false);
									}
								}else {
									/**文件已从列表删除*/
									/**重新遍历调取**/
									nextFile(task, false);
								}
							}
						}
					}
					break;
				default:
					//数据有误，停止播放
					WorkServerService.closeTask(task);
					return false;
			}
			return true;
    	} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
    }

	/**文件初始化读取
	 * @throws IOException */
	private static boolean fileRead(FileCastTask task,int step) throws IOException {
		try {
			task.lock.lock();
			task.getRunFile().destory();//关闭原文件读取信息
			RunningFile file = RunningFile.getRunningFile(task.getCastFileList().get(step));
			task.setRunFile(file);
			task.getTimer().reloadTimer();//重置定时器
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			task.lock.unlock();
		}
		return false;
	}
	/***
	 * 设置广播的节点
	 * @Title: fileplaySite
	 * @Description: 设置广播的节点
	 * @param task
	 * @param playSite
	 * @return boolean 返回类型
	 * @throws
	 * @author 10155
	 * @date 2020年4月20日 下午10:20:10
	 */
	public static boolean filePlaySite(FileCastTask task,Long playSite) {
		try {
			task.lock.lock();
			//TODO 按帧调节音频
			if(task.getRunFile().isFrame()) {
				if(playSite >= task.getRunFile().getPlaySite()) {
					/**向后调节音频*/
					//时间差
					long length = playSite -task.getRunFile().getPlaySite();
					//帧数差
					long frameSizes = length/task.getRunFile().getTimesize();
					task.getRunFile().getInStream().SkipFrame(frameSizes);
					task.getRunFile().setPlaySite(playSite);
					//安插一帧空白帧
					task.getRunFile().setBlankFrame(true);
				}else {
					/**向前调节音频*/
					long frameSizes = playSite/task.getRunFile().getTimesize();
					task.getRunFile().resetIn();
					task.getRunFile().getInStream().SkipFrame(frameSizes);
					task.getRunFile().setPlaySite(playSite);
					//安插一帧空白帧
					task.getRunFile().setBlankFrame(true);
				}
				return true;
			}else {
				if(playSite >= task.getRunFile().getPlaySite()) {
					/**向后调节音频*/
					//时间差
					long length = playSite -task.getRunFile().getPlaySite();
					long byteSizes = length * task.getRunFile().getBitsize()/task.getRunFile().getTimesize();
					task.getRunFile().loadPlaySite(byteSizes);
					task.getRunFile().setPlaySite(playSite);
				}else {
					/**向前调节音频*/
					long byteSizes = playSite * task.getRunFile().getBitsize()/task.getRunFile().getTimesize();
					task.getRunFile().resetIn();
					task.getRunFile().loadPlaySite(byteSizes);
					task.getRunFile().setPlaySite(playSite);
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			task.lock.unlock();
		}
		return false;
	}

	/**修改广播音量**/
    public static boolean castTaskVolChange(FileCastTask task,Integer vol) {
    	try {
    		task.setVol(vol);

    		/**发送音量指令*/
    		ByteBuffer endbs = InterCmdProcess.sendVolSet(vol,false);
    		try {
    			task.getServer().sendData(endbs);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
    }

    /**
     * 选择音频播放
     * @Title: castTaskPlayFile
     * @Description: 选择音频播放
     * @param task
     * @param fileId
     * @return boolean 返回类型
     * @throws
     * @author 10155
     * @date 2020年4月22日 下午9:47:15
     */
    public static boolean castTaskPlayFile(FileCastTask task,String fileId) {
    	try {
    		if(task.getRunFile().getFileId().equals(fileId)) {
    			/**同一文件*/
    			//重复一曲
				task.lock.lock();
				try {
					task.getRunFile().resetIn();
					task.getRunFile().setPlaySite(0);
				} catch (Exception e) {
					throw e;
				}finally {
					task.lock.unlock();
				}
    		}else {
				synchronized (task.findSongDataList()) {
					int oldStep = task.findSongDataList().indexOf(task.getRunFile().getFileId());
					int step = task.findSongDataList().indexOf(fileId);
					if(step >= 0) {
						if(!fileRead(task,step)) {
							fileRead(task,oldStep);
							task.removeWorkFile(step);
							return false;
						}
					}else {
						return false;
					}
				}
    		}
    		return true;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return false;
    }

    /**
     * 删除列表中指定音频
     * @Title: removeFile
     * @Description: 删除列表中指定音频
     * @param task
     * @param fileId
     * @return boolean 返回类型
     * @throws
     * @author 10155
     * @date 2020年4月22日 下午9:47:15
     */
    public static boolean removeFile(FileCastTask task,String fileId) {
    	try {
    		task.removeWorkFile(fileId);
    		return true;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return false;
    }
}
