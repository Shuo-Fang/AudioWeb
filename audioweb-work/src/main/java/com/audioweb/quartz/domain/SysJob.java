package com.audioweb.quartz.domain;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.audioweb.common.annotation.Excel;
import com.audioweb.common.annotation.Excel.ColumnType;
import com.audioweb.common.constant.ScheduleConstants;
import com.audioweb.common.core.domain.BaseEntity;
import com.audioweb.common.utils.DateUtils;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.quartz.util.CronUtils;

/**
 * 定时任务调度表 sys_job
 * 
 * @author ruoyi
 */
public class SysJob extends BaseEntity implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 任务ID */
    @Excel(name = "任务序号", cellType = ColumnType.NUMERIC)
    protected Long jobId;

    /** 任务名称 */
    @Excel(name = "任务名称")
    protected String jobName;

    /** 任务组名 */
    @Excel(name = "任务组名")
    protected String jobGroup;

    /** 调用目标字符串 */
    @Excel(name = "调用目标字符串")
    protected String invokeTarget;

    /** cron执行表达式 */
    @Excel(name = "执行表达式 ")
    protected String cronExpression;

    /** cron计划策略 */
    //@Excel(name = "计划策略 ", readConverterExp = "0=默认,1=立即触发执行,2=触发一次执行,3=不触发立即执行")
    protected String misfirePolicy = ScheduleConstants.MISFIRE_DEFAULT;

    /** 是否并发执行（0允许 1禁止） */
    @Excel(name = "并发执行", readConverterExp = "0=允许,1=禁止")
    protected String concurrent;
    
    /** 开始时间 */
    @Excel(name = "开始时间", dateFormat = DateUtils.YYYY_MM_DD_HH_MM_SS)
    protected Date startTime;
    
    /** 结束时间 */
    @Excel(name = "结束时间", dateFormat = DateUtils.YYYY_MM_DD_HH_MM_SS)
    protected Date endTime;
    
    /** 下次执行时间 */
    @Excel(name = "下次执行时间", dateFormat = DateUtils.YYYY_MM_DD_HH_MM_SS)
    protected Date nextValidTime;
    
    /** 任务状态（0正常 1暂停） */
    @Excel(name = "任务状态", readConverterExp = "0=正常,1=暂停")
    protected String status;

    public Long getJobId()
    {
        return jobId;
    }

    public void setJobId(Long jobId)
    {
        this.jobId = jobId;
    }

    @NotBlank(message = "任务名称不能为空")
    @Size(min = 0, max = 64, message = "任务名称不能超过64个字符")
    public String getJobName()
    {
        return jobName;
    }

    public void setJobName(String jobName)
    {
        this.jobName = jobName;
    }

    public String getJobGroup()
    {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup)
    {
        this.jobGroup = jobGroup;
    }

    @NotBlank(message = "调用目标字符串不能为空")
    @Size(min = 0, max = 1000, message = "调用目标字符串长度不能超过500个字符")
    public String getInvokeTarget()
    {
        return invokeTarget;
    }

    public void setInvokeTarget(String invokeTarget)
    {
        this.invokeTarget = invokeTarget;
    }

    @NotBlank(message = "Cron执行表达式不能为空")
    @Size(min = 0, max = 255, message = "Cron执行表达式不能超过255个字符")
    public String getCronExpression()
    {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression)
    {
        this.cronExpression = cronExpression;
    }

    public Date getNextValidTime() {
    	if (StringUtils.isNull(nextValidTime) && StringUtils.isNotEmpty(cronExpression)) {
            return CronUtils.getNextExecution(cronExpression,StringUtils.isNull(startTime)?DateUtils.getNowDate():startTime);
    	}
		return nextValidTime;
	}

	public void setNextValidTime(Date nextValidTime) {
		this.nextValidTime = nextValidTime;
	}

	public String getMisfirePolicy()
    {
        return misfirePolicy;
    }

    public void setMisfirePolicy(String misfirePolicy)
    {
        this.misfirePolicy = misfirePolicy;
    }

    public String getConcurrent()
    {
        return concurrent;
    }

    public void setConcurrent(String concurrent)
    {
        this.concurrent = concurrent;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
    
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("jobId", getJobId())
            .append("jobName", getJobName())
            .append("jobGroup", getJobGroup())
            .append("cronExpression", getCronExpression())
            .append("nextValidTime", getNextValidTime())
            .append("misfirePolicy", getMisfirePolicy())
            .append("concurrent", getConcurrent())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("remark", getRemark())
            .toString();
    }
}