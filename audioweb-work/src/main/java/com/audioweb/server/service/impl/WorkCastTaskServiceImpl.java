package com.audioweb.server.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.audioweb.work.domain.WorkCastTask;
import com.github.pagehelper.PageInfo;
import com.audioweb.common.core.text.Convert;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.server.service.IWorkCastTaskService;

/**
 * 广播任务Service业务层处理
 * 
 * @author shuofang
 * @date 2020-03-21
 */
@Service
public class WorkCastTaskServiceImpl implements IWorkCastTaskService 
{

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
    	tInfo.setPageSize((int)Math.ceil(list.size()));
        return tInfo.getList();
    }

    /**
     * 新增广播任务
     * 
     * @param workCastTask 广播任务
     * @return 结果
     */
    @Override
    public int insertWorkCastTask(WorkCastTask workCastTask)
    {
    	
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
}
