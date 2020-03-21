package com.audioweb.work.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.audioweb.work.mapper.WorkCastTaskMapper;
import com.audioweb.work.domain.WorkCastTask;
import com.audioweb.work.service.IWorkCastTaskService;
import com.audioweb.common.core.text.Convert;

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
    private WorkCastTaskMapper workCastTaskMapper;

    /**
     * 查询广播任务
     * 
     * @param taskId 广播任务ID
     * @return 广播任务
     */
    @Override
    public WorkCastTask selectWorkCastTaskById(Long taskId)
    {
        return workCastTaskMapper.selectWorkCastTaskById(taskId);
    }

    /**
     * 查询广播任务列表
     * 
     * @param workCastTask 广播任务
     * @return 广播任务
     */
    @Override
    public List<WorkCastTask> selectWorkCastTaskList(WorkCastTask workCastTask)
    {
        return workCastTaskMapper.selectWorkCastTaskList(workCastTask);
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
        return workCastTaskMapper.insertWorkCastTask(workCastTask);
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
        return workCastTaskMapper.updateWorkCastTask(workCastTask);
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
        return workCastTaskMapper.deleteWorkCastTaskByIds(Convert.toStrArray(ids));
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
        return workCastTaskMapper.deleteWorkCastTaskById(taskId);
    }

    /**
     * 系统初始化删除全部缓存任务
     * 
     * @return 结果
     */
	@Override
	public int deleteWorkCastTaskAll() {
		return workCastTaskMapper.deleteWorkCastTaskAll();
	}
}
