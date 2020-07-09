package com.audioweb.quartz.task;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.audioweb.common.constant.ScheduleConstants;
import com.audioweb.common.exception.job.TaskException;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.common.utils.spring.SpringUtils;
import com.audioweb.quartz.domain.SysJob;
import com.audioweb.server.service.impl.WorkCastTaskServiceImpl;
import com.audioweb.work.domain.WorkSchemeTask;
import com.audioweb.work.service.impl.WorkSchemeTaskServiceImpl;

/**
 * 定时任务任务分发实现类
 * @ClassName: TimeCastTask 
 * @Description: 定时任务任务分发实现类
 * @author 10155 1015510750@qq.com 
 * @date 2020年7月5日 下午2:48:59
 */
public class TimeCastTask {
    private static final Logger log = LoggerFactory.getLogger(TimeCastTask.class);
	/**
	 * 
	 * @Title: run 
	 * @Description: 任务执行类
	 * @param task void 返回类型 
	 * @throws Exception
	 * @author 10155 
	 * @date 2020年7月5日 下午3:01:22
	 */
	public static void doTimeCastTask(SysJob task) {
		log.info("开始执行定时任务  -- {}:{}",task.getInvokeTarget(),task.getJobName());
		if(StringUtils.isNotNull(task) && task instanceof WorkSchemeTask) {
			WorkSchemeTask schemeTask = (WorkSchemeTask) task;
			WorkSchemeTaskServiceImpl serviceImpl = SpringUtils.getBean(WorkSchemeTaskServiceImpl.class);
			WorkSchemeTask beanSchemeTask = serviceImpl.selectWorkSchemeTaskById(schemeTask.getSchemeTaskId());
			if(StringUtils.isNull(beanSchemeTask) || serviceImpl.isChangeTask(schemeTask, beanSchemeTask)) {
				log.warn("定时任务信息收取有误,进行更新中  -- {}:{}",task.getInvokeTarget(),task.getJobName());
				try {
					//定时任务构建信息改变
					serviceImpl.updateSchedulerJob(beanSchemeTask, schemeTask.getJobGroup());
				} catch (SchedulerException | TaskException e) {
					log.error("定时任务更新失败:",e);
				}
			}
			if(ScheduleConstants.Status.NORMAL.getValue().equals(beanSchemeTask.getWorkScheme().getStatus()) 
					&& ScheduleConstants.Status.NORMAL.getValue().equals(beanSchemeTask.getStatus())){
				log.info("定时任务开始创建广播任务  -- {}:{}",task.getInvokeTarget(),task.getJobName());
				WorkCastTaskServiceImpl castTaskService = SpringUtils.getBean(WorkCastTaskServiceImpl.class);
				/** 创建定时广播实体 **/
				//castTaskService.insertWorkCastTask(workCastTask);
				log.info("定时任务创建广播任务结束  -- {}:{}",task.getInvokeTarget(),task.getJobName());
			}else {
				log.info("定时任务设置中已暂停,定时任务结束  -- {}:{}",task.getInvokeTarget(),task.getJobName());
			}
		}else {
			log.warn("定时任务信息有误  -- {}:{}",task.getInvokeTarget(),task.getJobName());
		}
		log.info("结束执行定时任务  -- {}:{}",task.getInvokeTarget(),task.getJobName());
	}
}
