package com.audioweb.work.domain;

import com.audioweb.common.enums.FileCastType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.audioweb.common.annotation.Excel;
import com.audioweb.quartz.domain.SysJob;

public class WorkSchemeTask extends SysJob{
	private static final long serialVersionUID = 1L;
	
	/** 定时任务ID */
    private Long schemeTaskId;

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
    private FileCastType taskCastType;

    public void setSchemeTaskId(Long schemeTaskId)
    {
        this.schemeTaskId = schemeTaskId;
    }

    public Long getSchemeTaskId()
    {
        return schemeTaskId;
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
        this.taskCastType = FileCastType.valueOf(taskCastType);
    }

    public FileCastType getTaskCastType()
    {
        return taskCastType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("scheTaskId", getSchemeTaskId())
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
