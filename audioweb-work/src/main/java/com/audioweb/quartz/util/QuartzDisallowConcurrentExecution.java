package com.audioweb.quartz.util;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;

import com.audioweb.common.enums.CastWorkType;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.quartz.domain.SysJob;
import com.audioweb.quartz.task.TimeCastTask;

/**
 * 定时任务处理（禁止并发执行）
 * 
 * @author ruoyi
 *
 */
@DisallowConcurrentExecution
public class QuartzDisallowConcurrentExecution extends AbstractQuartzJob
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
