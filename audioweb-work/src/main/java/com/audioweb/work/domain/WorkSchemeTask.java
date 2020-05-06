package com.audioweb.work.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.audioweb.common.annotation.Excel;
import com.audioweb.quartz.domain.SysJob;

public class WorkSchemeTask extends SysJob{
	private static final long serialVersionUID = 1L;
	
	/** 定时任务ID */
    private Long scheTaskId;

    /** 任务对应sys_job的ID */
    private Long jobId;

    /** 广播任务方案ID */
    private Long schemeId;

    /** 广播事件的名字 */
    @Excel(name = "广播事件的名字")
    private String taskName;

    /** 任务定时时长 */
    @Excel(name = "任务定时时长(单位：秒)")
    private Long taskTiming;

    /** 音频信息字符串，逗号分隔 */
    @Excel(name = "音频信息字符串，逗号分隔")
    private String songData;

    /** 音量 */
    @Excel(name = "音量")
    private Integer taskVol;

    /** 广播任务分区信息 */
    private String taskDomainIds;

    /** 广播任务终端信息 */
    private String taskTerIds;

    /** ORDER("顺序播放"),LIST("列表循环"),RANDOM("随机播放"),SINGLE("单曲循环") */
    private String taskCastType;

    public void setScheTaskId(Long scheTaskId) 
    {
        this.scheTaskId = scheTaskId;
    }

    public Long getScheTaskId() 
    {
        return scheTaskId;
    }
    public void setJobId(Long jobId) 
    {
        this.jobId = jobId;
    }

    public Long getJobId() 
    {
        return jobId;
    }
    public void setSchemeId(Long schemeId) 
    {
        this.schemeId = schemeId;
    }

    public Long getSchemeId() 
    {
        return schemeId;
    }
    public void setTaskName(String taskName) 
    {
        this.taskName = taskName;
    }

    public String getTaskName() 
    {
        return taskName;
    }
    public void setTaskTiming(Long taskTiming) 
    {
        this.taskTiming = taskTiming;
    }

    public Long getTaskTiming() 
    {
        return taskTiming;
    }
    public void setSongData(String songData) 
    {
        this.songData = songData;
    }

    public String getSongData() 
    {
        return songData;
    }
    public void setTaskVol(Integer taskVol) 
    {
        this.taskVol = taskVol;
    }

    public Integer getTaskVol() 
    {
        return taskVol;
    }
    public void setTaskDomainIds(String taskDomainIds) 
    {
        this.taskDomainIds = taskDomainIds;
    }

    public String getTaskDomainIds() 
    {
        return taskDomainIds;
    }
    public void setTaskTerIds(String taskTerIds) 
    {
        this.taskTerIds = taskTerIds;
    }

    public String getTaskTerIds() 
    {
        return taskTerIds;
    }
    public void setTaskCastType(String taskCastType) 
    {
        this.taskCastType = taskCastType;
    }

    public String getTaskCastType() 
    {
        return taskCastType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("scheTaskId", getScheTaskId())
            .append("jobId", getJobId())
            .append("schemeId", getSchemeId())
            .append("taskName", getTaskName())
            .append("taskTiming", getTaskTiming())
            .append("songData", getSongData())
            .append("taskVol", getTaskVol())
            .append("taskDomainIds", getTaskDomainIds())
            .append("taskTerIds", getTaskTerIds())
            .append("taskCastType", getTaskCastType())
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
