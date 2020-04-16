package com.audioweb.server.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.audioweb.work.domain.FileCastTask;
import com.audioweb.work.domain.RunningFile;
import com.audioweb.work.domain.WorkCastTask;
import com.audioweb.work.domain.WorkFile;
import com.audioweb.work.domain.WorkTerminal;
import com.github.pagehelper.PageInfo;
import com.audioweb.common.core.domain.AjaxResult;
import com.audioweb.common.core.text.Convert;
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
        			if(fileCastTask.getCastTeridlist().size() > 0) {
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
    				result = AjaxResult.error("播放列表为空！");
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
     * @throws 抛出错误
     * @author ShuoFang 
     * @date 2020年4月9日 下午3:42:50
     */
    private void initTerTree(WorkCastTask castTask) {
    	ArrayList<WorkTerminal> taskTers = new ArrayList<>();
    	if(StringUtils.isNotEmpty(castTask.getDomainidlist())) {
    		List<String> doms = Convert.strToList(castTask.getDomainidlist());
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
    	if(StringUtils.isNotEmpty(castTask.getTeridlist())) {
    		taskTers.addAll(WorkTerminal.getTerByIds(castTask.getTeridlist()));
    	}
    	castTask.setCastTeridlist(taskTers);
    }
    /**
     * 初始化任务广播文件列表
     * @Title: initFile 
     * @Description: 初始化任务广播文件列表
     * @param castTask void 返回类型 
     * @throws 抛出错误
     * @author ShuoFang 
     * @date 2020年4月13日 下午1:53:32
     */
    private boolean initFile(FileCastTask castTask) {
    	List<WorkFile> taskFiles = new LinkedList<>();
    	if(StringUtils.isNotEmpty(castTask.findSongDataList())) {
    		List<String> songs = castTask.findSongDataList();
    		for(String song:songs) {
    			if(StringUtils.isNotEmpty(song)) {
    				WorkFile file = WorkFile.getFileById(song);
    				if(StringUtils.isNotNull(file)) {
    					taskFiles.add(file);
    				}
    			}
    		}
    	}
    	castTask.setCastFileList(taskFiles);
    	if(taskFiles.size() > 0) {
    		return true;
    	}else {
    		return false;
    	}
    }
	
}
