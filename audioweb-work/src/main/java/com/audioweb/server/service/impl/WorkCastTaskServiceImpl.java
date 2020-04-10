package com.audioweb.server.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.audioweb.work.domain.FileCastTask;
import com.audioweb.work.domain.WorkCastTask;
import com.audioweb.work.domain.WorkTerminal;
import com.github.pagehelper.PageInfo;
import com.audioweb.common.core.text.Convert;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.server.GroupNettyServer;
import com.audioweb.server.ServerManager;
import com.audioweb.server.service.IWorkCastTaskService;
import com.audioweb.system.domain.SysDomain;

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
    	if(StringUtils.isNotEmpty(workCastTask.getCastLevel())) {
    		for(int i = list.size()-1 ;i>=0 ;i--) {
    			if(Integer.parseInt(list.get(i).getCastLevel()) > Integer.parseInt(workCastTask.getCastLevel()) ) {
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
    public int insertWorkCastTask(WorkCastTask workCastTask)
    {
    	/**广播任务类型分类处理**/
    	if(StringUtils.isNotNull(workCastTask.getCastType())) {
        	switch (workCastTask.getCastType()) {
    		case FILE://文件广播 需要组播、文件管理、分区终端树、用户关联
    			initTerTree(workCastTask);//初始化分区终端树
    			FileCastTask fileCastTask = (FileCastTask) workCastTask;
    			fileCastTask.setServer(new GroupNettyServer(fileCastTask));
    			
    			
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
        return workCastTask.put()?1:0;
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
    		return task.remove()?1:0;
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
    	Set<WorkTerminal> taskTers = new HashSet<>();
    	if(StringUtils.isNotEmpty(castTask.getDomainidlist())) {
    		String[] doms = castTask.getDomainidlist().split(",");
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
    	castTask.setCastTeridlist(new ArrayList<WorkTerminal>(taskTers));
    }
}
