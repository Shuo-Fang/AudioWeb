package com.audioweb.work.domain;

import com.audioweb.common.enums.CastWorkType;
import com.audioweb.common.enums.FileCastType;
import com.audioweb.common.enums.TaskTimeType;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.audioweb.common.annotation.Excel;
import com.audioweb.quartz.domain.SysJob;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL) 
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
    
    /**任务类型  FILE("文件广播"),TIME("定时广播"),REAL("实时采播"),PLUG("控件广播"),
	WORD("文本广播"),POINT("终端点播"),CLIENT("终端采播"),PAGING("寻呼话筒") */
    private CastWorkType taskType;
    
    /**定时类型  DAILY("每日任务"),WEEKLY("每周任务"),MONTHLY("每月任务"),SINGLE("单次任务"),CUSTOM("自定任务")*/
    private TaskTimeType taskTimeType;
    
    /** 是否使用定时时长task_timing */
    private Boolean taskIsTiming;
    
    /** 任务正在执行次数 */
    private Integer runningTimes;
    
    /** 所属广播方案 */
    private WorkScheme workScheme;

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
        this.taskCastType = FileCastType.invokeEnum(taskCastType);
    }

    public FileCastType getTaskCastType()
    {
        return taskCastType;
    }

    public CastWorkType getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = CastWorkType.invokeEnum(taskType);
	}
	
	public TaskTimeType getTaskTimeType() {
		return taskTimeType;
	}

	public void setTaskTimeType(String taskTimeType) {
		this.taskTimeType = TaskTimeType.invokeEnum(taskTimeType);
	}

	public Boolean getTaskIsTiming() {
		return taskIsTiming;
	}

	public void setTaskIsTiming(Boolean taskIsTiming) {
		this.taskIsTiming = taskIsTiming;
	}

	public Integer getRunningTimes() {
		return runningTimes;
	}

	public void setRunningTimes(Integer runningTimes) {
		this.runningTimes = runningTimes;
	}

	public WorkScheme getWorkScheme() {
		return workScheme;
	}

	public void setWorkScheme(WorkScheme workScheme) {
		this.workScheme = workScheme;
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
