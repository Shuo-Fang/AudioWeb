package com.audioweb.server.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.audioweb.work.domain.FileCastTask;
import com.audioweb.work.domain.WorkCastTask;
import com.audioweb.work.domain.WorkFile;
import com.audioweb.work.domain.WorkTerminal;
import com.audioweb.work.service.IWorkFileService;
import com.github.pagehelper.PageInfo;
import com.audioweb.common.constant.WorkConstants;
import com.audioweb.common.core.domain.AjaxResult;
import com.audioweb.common.core.text.Convert;
import com.audioweb.common.enums.FileCastCommand;
import com.audioweb.common.enums.FileCastType;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.server.GroupNettyServer;
import com.audioweb.server.ServerManager;
import com.audioweb.server.service.IWorkCastTaskService;
import com.audioweb.server.service.TimeFileCast;
import com.audioweb.server.service.WorkFileTaskService;
import com.audioweb.server.service.WorkServerService;

/**
 * 广播任务Service业务层处理
 * 
 * @author shuofang
 * @date 2020-03-21
 */
@Service
public class WorkCastTaskServiceImpl implements IWorkCastTaskService 
{
	
	@Autowired
	ServerManager serverManager;
    @Autowired
    private IWorkFileService workFileService;
    /**
     * 查询广播任务
     * 
     * @param taskId 广播任务ID
     * @return 广播任务
     */
    @Override
    public WorkCastTask selectWorkCastTaskById(Long taskId)
    {
        return WorkCastTask.find(taskId);
    }

    /**
     * 查询广播任务列表
     * 
     * @param workCastTask 广播任务
     * @return 广播任务
     */
    @Override
    public List<WorkCastTask> selectWorkCastTaskList(WorkCastTask workCastTask,Integer pageNum,Integer pageSize)
    {
    	List<WorkCastTask> list = workCastTask.export();
    	List<WorkCastTask> result = new ArrayList<WorkCastTask>();
    	/**先做筛选*/
    	if(StringUtils.isNotEmpty(workCastTask.getTaskName())) {
    		for(int i = list.size()-1 ;i>=0 ;i--) {
    			if(list.get(i).getTaskName().indexOf(workCastTask.getTaskName()) < 0) {
    				list.remove(i);
    			}
    		}
    	}
    	if(StringUtils.isNotNull(workCastTask.getCastType())) {
    		for(int i = list.size()-1 ;i>=0 ;i--) {
    			if(list.get(i).getCastType() != workCastTask.getCastType()) {
    				list.remove(i);
    			}
    		}
    	}
    	if(StringUtils.isNotNull(workCastTask.getCastLevel())) {
    		for(int i = list.size()-1 ;i>=0 ;i--) {
    			if(list.get(i).getCastLevel() > workCastTask.getCastLevel() ) {
    				list.remove(i);
    			}
    		}
    	}
    	Collections.sort(list);
    	/**总数*/
    	int size = list.size();
    	/**分页过滤*/
    	for(int i = (pageNum-1)*pageSize;i<pageNum*pageSize && i < size;i++) {
    		result.add(list.get(i));
    	}
    	PageInfo<WorkCastTask> tInfo = new PageInfo<WorkCastTask>(result);
    	tInfo.setTotal(list.size());
    	tInfo.setPageNum(pageNum);
    	tInfo.setPageSize(pageSize);
    	tInfo.setSize(result.size());
    	tInfo.setPages((int)Math.ceil(list.size()/pageSize*1.0));
        return tInfo.getList();
    }

    /**
     * 新增与创建广播任务
     * 
     * @param workCastTask 广播任务
     * @return 结果
     */
    @Override
    public AjaxResult insertWorkCastTask(WorkCastTask workCastTask)
    {
    	AjaxResult result = AjaxResult.success();
    	/**广播任务类型分类处理**/
    	if(StringUtils.isNotNull(workCastTask.getCastType())) {
        	switch (workCastTask.getCastType()) {
    		case FILE://文件广播 需要组播、文件管理、分区终端树、用户关联
    			/**初始化文件管理**/
    			FileCastTask fileCastTask = (FileCastTask) workCastTask;
    			if(initFile(fileCastTask)) {
        			initTerTree(fileCastTask);//初始化分区终端树
        			if(fileCastTask.getCastTeridList().size() > 0) {
	        			fileCastTask.setServer(new GroupNettyServer(fileCastTask));//开启组播
	        			/**初始化正在广播文件**/
	    				if(WorkFileTaskService.initFileRead(fileCastTask)) {
	            			/**初始化定时分发音频任务线程**/
	    					fileCastTask.setTimer(new TimeFileCast(fileCastTask));
	            			/**初始化广播命令群发*/
	            			WorkServerService.addAltTasks(fileCastTask);
	    				}else {
	    					result = AjaxResult.error("初始化正在播放音频格式有误！");
	    				}
	        			fileCastTask.put();
	        			result.put(AjaxResult.DATA_TAG, fileCastTask);
        			}else {
        				result = AjaxResult.error("选中终端列表为空！");
        			}
    			}else {
    				/**没有文件**/
    				result = AjaxResult.error("播放列表为空或有列表音频有误！");
    			}
    			break;
    		case TIME://定时广播 需要组播、文件管理、分区终端树、定时控制
    			
    			break;
    		case REAL://实时广播 需要组播、分区终端树、WebSocket关联
    			
    			break;
    		case PLUG://控件广播 需要组播、分区终端树、WebSocket关联(可能)
    			
    			break;
    		default:
    			break;
    		}
    	}
        return result;
    }
    
    /**
     * 调节广播中文件列表顺序
     * 
     * @param taskId 广播任务Id
     * @param fileId 广播文件Id
     * @param site 广播文件排序号
     * @return 结果
     */
    @Override
    public AjaxResult sortFileInFileCast(Long taskId,String fileId, Integer site)
    {
    	WorkCastTask task = WorkCastTask.find(taskId);
    	if(StringUtils.isNotNull(task) && task instanceof FileCastTask) {
    		FileCastTask tCastTask = (FileCastTask) task;
    		if(tCastTask.findSongDataList().contains(fileId)) {
    			if(StringUtils.isNotNull(site) && site >= 0 && site < tCastTask.findSongDataList().size()) {
    				if(tCastTask.sortWorkFile(fileId,site)) {
    					return AjaxResult.success();
    				}else {
    					return AjaxResult.error("修改出错，请刷新页面重试！");
    				}
    			}else {
    				return AjaxResult.error("指定节点有误，请刷新页面重试！");
    			}
    		}else {
    			return AjaxResult.error("所选音频不在音频列表中，请刷新页面重试！");
    		}
    	}
    	return AjaxResult.error("操作出错或任务已结束！");
    }

    /**
     * 重置文件广播中文件列表
     * 
     * @param taskId 广播任务Id
     * @param songData 广播文件Id组
     * @return 结果
     */
    @Override
    public AjaxResult reloadFileInFileCast(Long taskId,String songData)
    {
    	WorkCastTask task = WorkCastTask.find(taskId);
    	if(StringUtils.isNotNull(task) && task instanceof FileCastTask) {
    		FileCastTask tCastTask = (FileCastTask) task;
    		if(reloadFile(tCastTask,songData)) {
    			return AjaxResult.success();
    		}else {
    			return AjaxResult.error("操作失败或所选音频信息为空！");
    		}
    	}
    	return AjaxResult.error("操作出错或任务已结束！");
    }
    /**
     * 文件广播删除指定文件
     * 
     * @param taskId 广播任务Id
     * @param fileIds 广播文件Id组
     * @return 结果
     */
    @Override
    public AjaxResult removeFileInFileCast(Long taskId,String fileIds)
    {
    	WorkCastTask task = WorkCastTask.find(taskId);
    	if(StringUtils.isNotNull(task) && task instanceof FileCastTask) {
    		FileCastTask tCastTask = (FileCastTask) task;
    		List<String> fStrings = Convert.strToList(fileIds);
    		if(fStrings.size() == 0) { 
    			return AjaxResult.error("指定的音频信息为空!");
    		}else if(fStrings.size() == 1) {
        		if(!tCastTask.findSongDataList().contains(fStrings.get(0))) {
        			return AjaxResult.error("指定的音频未在广播列表中!");
        		}
    			if(tCastTask.getRunFile().getFileId().equals(fStrings.get(0))) {
    				return AjaxResult.error("无法删除正在播放的音频!");
    			}
        		if(WorkFileTaskService.removeFile(tCastTask,fStrings.get(0))) {
        			return AjaxResult.success("删除成功！");
        		}else {
        			return AjaxResult.error("删除出错或找不到指定音频！");
        		}
    		}else {
    			return removeFileList(tCastTask,fStrings);
    		}
    	}
    	return AjaxResult.error("删除出错或任务已结束！");
    }
    /**
     * 文件广播播放指定文件
     * 
     * @param taskId 广播任务Id
     * @param fileId 广播文件Id
     * @return 结果
     */
    @Override
    public AjaxResult controlFileCast(Long taskId,String fileId)
    {
    	WorkCastTask task = WorkCastTask.find(taskId);
    	if(StringUtils.isNotNull(task) && task instanceof FileCastTask) {
    		FileCastTask tCastTask = (FileCastTask) task;
    		if(!tCastTask.findSongDataList().contains(fileId)) {
    			return AjaxResult.error("指定的音频未在广播列表中!");
    		}
    		if(WorkFileTaskService.castTaskPlayFile(tCastTask, fileId)) {
    			AjaxResult result = AjaxResult.success();
    			result.put(AjaxResult.DATA_TAG, tCastTask);
    			return result;
    		}else {
    			return AjaxResult.error("修改出错或找不到指定音频！");
    		}
    	}
    	return AjaxResult.error("修改出错或任务已结束！");
    }
    /**
     * 修改广播任务暂停启动，上下一曲
     * 
     * @param taskId 广播任务Id
     * @param command 广播任务命令
     * @return 结果
     */
    @Override
    public AjaxResult controlFileCast(Long taskId,FileCastCommand command)
    {
    	WorkCastTask task = WorkCastTask.find(taskId);
    	if(StringUtils.isNotNull(task) && task instanceof FileCastTask) {
    		FileCastTask tCastTask = (FileCastTask) task;
    		return fileCastCommand(tCastTask, command);
    	}
		return AjaxResult.error("修改出错或任务已结束！");
    }
    /**
     * 修改广播任务播放进度
     * 
     * @param taskId 广播任务Id
     * @param playSite 广播节点
     * @return 结果
     */
    @Override
    public AjaxResult controlFileCast(Long taskId,Long playSite)
    {
    	WorkCastTask task = WorkCastTask.find(taskId);
    	if(StringUtils.isNotNull(task) && task instanceof FileCastTask && StringUtils.isNotNull(playSite)) {
    		FileCastTask tCastTask = (FileCastTask) task;
    		if(playSite >= tCastTask.getRunFile().getDuration()) {
    			/**进度条大于或等于最大值,等于下一曲*/
    			return fileCastCommand(tCastTask, FileCastCommand.NEXT);
    		} else {
				if(WorkFileTaskService.filePlaySite(tCastTask, playSite)) {
					AjaxResult result = AjaxResult.success();
					result.put(AjaxResult.DATA_TAG, tCastTask);
					return result;
				}else {
					return AjaxResult.error("修改出错,请刷新重试！");
				}
			}
    	}
    	return AjaxResult.error("修改出错或任务已结束！");
    }
    /**
     * 修改广播任务播放模式
     * 
     * @param taskId 广播任务
     * @param type 广播模式
     * @return 结果
     */
    @Override
    public AjaxResult controlFileCast(Long taskId,FileCastType type)
    {
    	WorkCastTask task = WorkCastTask.find(taskId);
    	if(StringUtils.isNotNull(task) && task instanceof FileCastTask) {
    		FileCastTask tCastTask = (FileCastTask) task;
    		tCastTask.setFileCastType(type);
    		return AjaxResult.success();
    	}
		return AjaxResult.error("修改出错或任务已结束！");
    }
    
    /**
     * 修改广播任务播放音量
     * 
     * @param taskId 广播任务Id
	 * @param vol 音量
     * @return 结果
     */
    @Override
    public AjaxResult controlFileCast(Long taskId,Integer vol)
    {
    	WorkCastTask task = WorkCastTask.find(taskId);
    	if(StringUtils.isNotNull(task) && task instanceof FileCastTask) {
    		FileCastTask tCastTask = (FileCastTask) task;
    		if(StringUtils.isNotNull(vol)) {
    			if(vol < 0) {
    				vol = 0;
    			}else if (vol > 40) {
					vol = 40;
				}
    			if(WorkFileTaskService.castTaskVolChange(tCastTask,vol)) {
    				return AjaxResult.success();
    			}else {
    				return AjaxResult.error("修改出错！请刷新页面重试");
    			}
    		}
    	}
		return AjaxResult.error("修改出错或任务已结束！");
    }
    
    /**
     * 修改广播任务
     * 
     * @param workCastTask 广播任务
     * @return 结果
     */
    @Override
    public int updateWorkCastTask(WorkCastTask workCastTask)
    {
        return 0;
    }

    /**
     * 删除广播任务对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteWorkCastTaskByIds(String ids)
    {
    	WorkCastTask task = WorkCastTask.find(Long.parseLong(ids));
    	if(StringUtils.isNotNull(task)) {
    		WorkServerService.closeTask(task);
    		return 1;
    	}else {
    		return -1;
    	}
    }

    /**
     * 删除广播任务信息
     * 
     * @param taskId 广播任务ID
     * @return 结果
     */
    @Override
    public int deleteWorkCastTaskById(Long taskId)
    {
        return 0;
    }
    /**
     * 初始化任务分区终端树
     * @Title: initTerTree 
     * @Description: 初始化任务分区终端树
     * @param castTask void 返回类型 
     * @throws
     * @author ShuoFang 
     * @date 2020年4月9日 下午3:42:50
     */
    private void initTerTree(WorkCastTask castTask) {
    	ArrayList<WorkTerminal> taskTers = new ArrayList<>();
    	if(StringUtils.isNotEmpty(castTask.getDomainIdList())) {
    		List<String> doms = Convert.strToList(castTask.getDomainIdList());
    		for(String dom:doms) {
    			if(StringUtils.isNotEmpty(dom)) {
    				try {
    					if(!dom.contains("_")) {//全选,获取全部终端
    						taskTers.addAll(WorkTerminal.listTerByDomainId(Long.parseLong(dom)));
    					}
					} catch (Exception e) {
						e.printStackTrace();
					}
    			}
    		}
    	}
    	if(StringUtils.isNotEmpty(castTask.getTerIdList())) {
    		taskTers.addAll(WorkTerminal.getTerByIds(castTask.getTerIdList()));
    	}
    	castTask.setCastTeridList(taskTers);
    }
    /**
     * 初始化任务广播文件列表
     * @Title: initFile 
     * @Description: 初始化任务广播文件列表
     * @param castTask void 返回类型 
     * @throws
     * @author ShuoFang 
     * @date 2020年4月13日 下午1:53:32
     */
    private boolean initFile(FileCastTask castTask) {
    	List<WorkFile> taskFiles = new LinkedList<>();
    	if(StringUtils.isNotEmpty(castTask.getSongData())) {
    		taskFiles = workFileService.selectWorkFileByIds(castTask.getSongData());
    	}
    	/**去除非正常的音频信息**/
    	for(int size = taskFiles.size()-1; size >= 0; size--) {
    		if(!taskFiles.get(size).getDelFlag().equals(WorkConstants.NORMAL)) {
    			taskFiles.remove(size);
    			castTask.findSongDataList().remove(size);
    		}
    	}
    	castTask.setCastFileList(taskFiles);
    	if(taskFiles.size() > 0) {
    		return true;
    	}else {
    		return false;
    	}
    }
    /**
     * 重置任务广播文件列表
     * @Title: initFile 
     * @Description: 初始化任务广播文件列表
     * @param castTask void 返回类型 
     * @throws
     * @author ShuoFang 
     * @date 2020年4月13日 下午1:53:32
     */
    private boolean reloadFile(FileCastTask castTask,String songData) {
    	List<WorkFile> taskFiles = new LinkedList<>();
    	if(StringUtils.isNotEmpty(songData)) {
    		taskFiles = workFileService.selectWorkFileByIds(songData);
    	}if(taskFiles.size() > 0 && songData.contains(castTask.getRunFile().getFileId())) {
    		synchronized (castTask.getCastFileList()) {
	        	castTask.setCastFileList(taskFiles);
	        	castTask.setSongData(songData);
    		}
    		return true;
    	}else {
    		return false;
    	}
    }
	/**
	 * 控制文件广播上下一曲、暂停启动命令
	 * @Title: fileCastCommand 
	 * @Description: 控制文件广播上下一曲、暂停启动命令
	 * @param task 广播任务
	 * @param command 广播命令
	 * @return AjaxResult 返回类型 
	 * @throws
	 * @author 10155 
	 * @date 2020年4月19日 下午11:55:26
	 */
    private AjaxResult fileCastCommand(FileCastTask task, FileCastCommand command) {
    	AjaxResult result = AjaxResult.success(); 
    	switch (command) {
		case PREV:
			if(WorkFileTaskService.nextFile(task,false)) {
				result.put(AjaxResult.DATA_TAG, task);
				return result;
			}else {
				AjaxResult.error("控制失败，请刷新重试");
			}
			break;
		case PAUSE:
			task.setIsStop(true);
			result.put(AjaxResult.DATA_TAG, task);
			return result;
		case RESUME:
			task.setIsStop(false);
			result.put(AjaxResult.DATA_TAG, task);
			return result;
		case NEXT:
			if(WorkFileTaskService.nextFile(task,true)) {
				result.put(AjaxResult.DATA_TAG, task);
				return result;
			}else {
				AjaxResult.error("控制失败，请刷新重试");
			}
			break;
		default:
			AjaxResult.error("命令格式有误！");
			break;
		}
    	return AjaxResult.error("命令格式有误！");
    }
    
    private AjaxResult removeFileList(FileCastTask task,List<String> fIds) {
    	AjaxResult result;
    	if(fIds.remove(task.getRunFile().getFileId())) {
    		result = AjaxResult.warn("所选音频中无法删除正在广播文件！");
    	}else {
    		result = AjaxResult.success("批量删除成功");
    	}
    	for(String id:fIds) {
    		WorkFileTaskService.removeFile(task,id);
    	}
		return result;
	}
}
