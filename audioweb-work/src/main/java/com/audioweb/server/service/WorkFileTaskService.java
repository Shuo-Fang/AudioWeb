/**   
 * @Title: WorkFileTaskService.java 
 * @Package com.audioweb.server.service 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com
 * @date 2020年4月13日 下午1:50:18 
 * @version V1.0   
 */ 
package com.audioweb.server.service;

import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

import com.audioweb.common.utils.StringUtils;
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
				/**表示为自动播放完成后结束调用，先判断播放类型*/
				switch (task.getFileCastType()) {
					case ORDER://顺序播放
						//判断正在播放音频是否为最后一个音频
						synchronized (task.findSongDataList()) {
							if(Objects.equals(task.getRunFile().getFileId(), task.findSongDataList().get(task.findSongDataList().size()-1))) {
								//停止广播
								
							}else {
								//下一曲并继续播放
								int step = task.findSongDataList().indexOf(task.getRunFile().getFileId());
								FileRead(task,step);
							}
						}
						break;
					case LIST://列表循环
						//下一曲并继续播放
						synchronized (task.findSongDataList()) {
							int step = task.findSongDataList().indexOf(task.getRunFile().getFileId());
							if(step >= task.findSongDataList().size()-1) {
								step = -1;
							}
							FileRead(task,step);
						}
						break;
					case SINGLE://单曲循环
						//重复一曲
						synchronized (task.getRunFile()) {
							task.getRunFile().resetIn();
						}
						break;
					case RANDOM://随机播放
						//下一曲并继续播放
						synchronized (task.findSongDataList()) {
							Random random = new Random();
							int size = task.findSongDataList().size();
							int step = -1;
							if(size > 1) {//列表文件数大于1，才有随机的必要
								int location = task.findSongDataList().indexOf(task.getRunFile().getFileId());
								step = random.nextInt(task.findSongDataList().size());
								while(location == step) {//去除重复播放
									step = random.nextInt(task.findSongDataList().size());
								}
							}
							FileRead(task,step);
						}
						break;
					default:
						//数据有误，停止播放
						
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
	
	/**文件初始化读取
	 * @throws IOException */
	private static void FileRead(FileCastTask task,int step) throws IOException {
		synchronized (task.getRunFile()) {
			task.getRunFile().destory();//关闭原文件读取信息
			RunningFile file = RunningFile.getRunningFile(task.getCastFileList().get(step+1));
			task.setRunFile(file);
			task.getTimer().reloadTimer();//重置定时器
		}
	}
}
