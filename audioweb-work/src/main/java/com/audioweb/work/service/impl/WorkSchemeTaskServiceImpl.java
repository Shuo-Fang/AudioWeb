package com.audioweb.work.service.impl;

import java.util.List;
import java.util.Objects;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.audioweb.work.mapper.WorkSchemeTaskMapper;
import com.audioweb.work.domain.WorkSchemeTask;
import com.audioweb.work.service.IWorkSchemeTaskService;
import com.audioweb.common.constant.ScheduleConstants;
import com.audioweb.common.core.text.Convert;
import com.audioweb.common.exception.job.TaskException;
import com.audioweb.common.exception.job.TaskException.Code;
import com.audioweb.quartz.domain.SysJob;
import com.audioweb.quartz.mapper.SysJobMapper;
import com.audioweb.quartz.util.ScheduleUtils;

/**
 * 定时任务Service业务层处理
 * 
 * @author shuofang
 * @date 2020-05-09
 */
@Service
public class WorkSchemeTaskServiceImpl implements IWorkSchemeTaskService 
{
    @Autowired
    private Scheduler scheduler;
    
    @Autowired
    private SysJobMapper jobMapper;
    
    @Autowired
    private WorkSchemeTaskMapper workSchemeTaskMapper;

    /**
     * 查询定时任务
     * 
     * @param schemeTaskId 定时任务ID
     * @return 定时任务
     */
    @Override
    public WorkSchemeTask selectWorkSchemeTaskById(Long schemeTaskId)
    {
        return workSchemeTaskMapper.selectWorkSchemeTaskById(schemeTaskId);
    }

    /**
     * 查询定时任务列表
     * 
     * @param workSchemeTask 定时任务
     * @return 定时任务
     */
    @Override
    public List<WorkSchemeTask> selectWorkSchemeTaskList(WorkSchemeTask workSchemeTask)
    {
        return workSchemeTaskMapper.selectWorkSchemeTaskList(workSchemeTask);
    }

    /**
     * 暂停任务
     * 
     * @param job 调度信息
     */
    @Override
    @Transactional
    public int pauseSchemeTask(WorkSchemeTask workSchemeTask) throws SchedulerException
    {
        Long jobId = workSchemeTask.getJobId();
        String jobGroup = workSchemeTask.getJobGroup();
        workSchemeTask.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        int rows = jobMapper.updateJob(workSchemeTask);
        if (rows > 0)
        {
            scheduler.pauseJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }
    
    /**
     * 恢复任务
     * 
     * @param job 调度信息
     */
    @Override
    @Transactional
    public int resumeSchemeTask(WorkSchemeTask workSchemeTask) throws SchedulerException
    {
        Long jobId = workSchemeTask.getJobId();
        String jobGroup = workSchemeTask.getJobGroup();
        workSchemeTask.setStatus(ScheduleConstants.Status.NORMAL.getValue());
        workSchemeTask.setNextValidTime(scheduler.getTrigger(ScheduleUtils.getTriggerKey(jobId, jobGroup)).getNextFireTime());
        int rows = jobMapper.updateJob(workSchemeTask);
        if (rows > 0)
        {
            scheduler.resumeJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }
    /**
     * 新增任务
     * 
     * @param job 调度信息 调度信息
     */
    @Override
    @Transactional
    public int insertWorkSchemeTask(WorkSchemeTask workSchemeTask) throws SchedulerException, TaskException
    {
        int rows = jobMapper.insertJob(workSchemeTask);
        if (rows > 0)
        {
        	int rows2 = workSchemeTaskMapper.insertWorkSchemeTask(workSchemeTask);
        	if(rows2 > 0) {
        		ScheduleUtils.createScheduleJob(scheduler, workSchemeTask);
        	}else {
        		throw new TaskException("定时信息保存成功但定时任务未保存成功", Code.SCHEME_ASTK_ERROR);
        	}
        }
        return rows;
    }
    
    /**
     * 修改定时任务
     * 
     * @param workSchemeTask 定时任务
     * @return 结果
     */
    @Override
    @Transactional
    public int updateWorkSchemeTask(WorkSchemeTask workSchemeTask) throws SchedulerException, TaskException
    {
        SysJob properties = jobMapper.selectJobById(workSchemeTask.getJobId());
        int rows = jobMapper.updateJob(workSchemeTask);
        if (rows > 0)
        {
        	int rows2 = workSchemeTaskMapper.updateWorkSchemeTask(workSchemeTask);
	    	if(rows2 > 0) {
	        	if(isChangeTask(properties,workSchemeTask)) {
		    		//定时任务构建信息改变
		    		updateSchedulerJob(workSchemeTask, properties.getJobGroup());
		        }
	    	}else {
	    		throw new TaskException("定时信息保存成功但定时任务未保存成功", Code.SCHEME_ASTK_ERROR);
	    	}
        }
        return rows;
    }

    /**
     * 更新任务
     * 
     * @param job 任务对象
     * @param jobGroup 任务组名
     */
    public void updateSchedulerJob(SysJob job, String jobGroup) throws SchedulerException, TaskException
    {
        Long jobId = job.getJobId();
        // 判断是否存在
        JobKey jobKey = ScheduleUtils.getJobKey(jobId, jobGroup);
        if (scheduler.checkExists(jobKey))
        {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(jobKey);
        }
        ScheduleUtils.createScheduleJob(scheduler, job);
    }
    

    /**
     * 删除任务后，所对应的trigger也将被删除
     * 
     * @param job 调度信息
     */
    @Override
    @Transactional
    public int deleteWorkSchemeTaskById(WorkSchemeTask workSchemeTask) throws SchedulerException
    {
        Long jobId = workSchemeTask.getJobId();
        Long schemeTaskId = workSchemeTask.getSchemeTaskId();
        String jobGroup = workSchemeTask.getJobGroup();
        int rows = jobMapper.deleteJobById(jobId);
        if (rows > 0)
        {
        	scheduler.deleteJob(ScheduleUtils.getJobKey(jobId, jobGroup));
            workSchemeTaskMapper.deleteWorkSchemeTaskById(schemeTaskId);
        }
        return rows;
    }
    
    /**
     * 批量删除调度信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteWorkSchemeTaskByIds(String ids) throws SchedulerException
    {
        List<WorkSchemeTask> workSchemeTasks = workSchemeTaskMapper.selectWorkSchemeTaskByIds(Convert.toStrArray(ids));
        for (WorkSchemeTask job : workSchemeTasks)
        {
        	deleteWorkSchemeTaskById(job);
        }
        return workSchemeTasks.size();
    }
    /**
     * 定时任务定时控制信息是否变更
     * @Title: isChangeTask 
     * @Description: 定时任务定时控制信息是否变更
     * @param oldJob 旧任务
     * @param newJob 新任务
     * @return boolean 返回类型 
     * @throws 
     * @author 10155 
     * @date 2020年7月5日 上午10:45:12
     */
    public boolean isChangeTask(SysJob oldJob,SysJob newJob) {
    	if(!Objects.equals(oldJob.getJobGroup(),newJob.getJobGroup())) {
    		return true;
    	}else if(!Objects.equals(oldJob.getCronExpression(),newJob.getCronExpression())) {
    		return true;
    	}else if(!Objects.equals(oldJob.getStartTime(), newJob.getStartTime())) {
    		return true;
    	}else if(!Objects.equals(oldJob.getEndTime(), newJob.getEndTime())) {
    		return true;
    	}else if(!Objects.equals(oldJob.getInvokeTarget(), newJob.getInvokeTarget())) {
    		return true;
    	}
    	return false;
	}
}