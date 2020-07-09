package com.audioweb.quartz.util;

import org.quartz.JobExecutionContext;

import com.audioweb.common.enums.CastWorkType;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.quartz.domain.SysJob;
import com.audioweb.quartz.task.TimeCastTask;

/**
 * 定时任务处理（允许并发执行）
 * 
 * @author ruoyi
 *
 */
public class QuartzJobExecution extends AbstractQuartzJob
{
    @Override
    protected void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception
    {
    	CastWorkType type = CastWorkType.invokeEnum(sysJob.getInvokeTarget());
    	if(StringUtils.isNull(type)) {
    		JobInvokeUtil.invokeMethod(sysJob);
    	}else {
    		switch (type) {
    		case FILE:
    		case TIME:
    		case WORD:
    			TimeCastTask.doTimeCastTask(sysJob);
    			break;
    		case CLIENT:
    			
    			break;
    		default:
    			JobInvokeUtil.invokeMethod(sysJob);
    			break;
    		}
    	}
    }
}
