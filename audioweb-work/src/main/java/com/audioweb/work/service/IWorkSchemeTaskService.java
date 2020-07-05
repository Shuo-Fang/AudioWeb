package com.audioweb.work.service;

import java.util.List;

import org.quartz.SchedulerException;

import com.audioweb.common.exception.job.TaskException;
import com.audioweb.work.domain.WorkSchemeTask;

/**
 * 定时任务Service接口
 * 
 * @author shuofang
 * @date 2020-05-09
 */
public interface IWorkSchemeTaskService 
{
    /**
     * 查询定时任务
     * 
     * @param schemeTaskId 定时任务ID
     * @return 定时任务
     */
    public WorkSchemeTask selectWorkSchemeTaskById(Long schemeTaskId);

    /**
     * 查询定时任务列表
     * 
     * @param workSchemeTask 定时任务
     * @return 定时任务集合
     */
    public List<WorkSchemeTask> selectWorkSchemeTaskList(WorkSchemeTask workSchemeTask);

    /**
     * 新增定时任务
     * 
     * @param workSchemeTask 定时任务
     * @return 结果
     * @throws TaskException 
     * @throws SchedulerException 
     */
    public int insertWorkSchemeTask(WorkSchemeTask workSchemeTask) throws SchedulerException, TaskException;

    /**
     * 修改定时任务
     * 
     * @param workSchemeTask 定时任务
     * @return 结果
     * @throws TaskException 
     * @throws SchedulerException 
     */
    public int updateWorkSchemeTask(WorkSchemeTask workSchemeTask) throws SchedulerException, TaskException;

    /**
     * 批量删除定时任务
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     * @throws SchedulerException 
     */
    public int deleteWorkSchemeTaskByIds(String ids) throws SchedulerException;

	/**
	 * 暂停任务
	 * 
	 * @param job 调度信息
	 */
    public int pauseSchemeTask(WorkSchemeTask workSchemeTask) throws SchedulerException;

	/**
	 * 删除任务后，所对应的trigger也将被删除
	 * 
	 * @param job 调度信息
	 */
    public int deleteWorkSchemeTaskById(WorkSchemeTask workSchemeTask) throws SchedulerException;

	/**
	 * 恢复任务
	 * 
	 * @param job 调度信息
	 */
	int resumeSchemeTask(WorkSchemeTask workSchemeTask) throws SchedulerException;
}