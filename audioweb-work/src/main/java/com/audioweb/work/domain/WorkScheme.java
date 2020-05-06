package com.audioweb.work.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.audioweb.common.annotation.Excel;
import com.audioweb.common.core.domain.BaseEntity;

/**
 * 广播方案对象 work_scheme
 * 
 * @author shuofang
 * @date 2020-04-28
 */
public class WorkScheme extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 广播任务方案ID */
    private Long schemeId;

    /** 方案名称 */
    @Excel(name = "方案名称")
    private String schemeName;

    /** 方案状态（0正常 1停用） */
    @Excel(name = "方案状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 方案优先级 */
    @Excel(name = "方案优先级")
    private String schemePriority;

    /** 方案任务数 */
    private int taskCount;

    public int getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }

    public void setSchemeId(Long schemeId)
    {
        this.schemeId = schemeId;
    }

    public Long getSchemeId() 
    {
        return schemeId;
    }
    public void setSchemeName(String schemeName) 
    {
        this.schemeName = schemeName;
    }

    public String getSchemeName() 
    {
        return schemeName;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }
    public void setSchemePriority(String schemePriority) 
    {
        this.schemePriority = schemePriority;
    }

    public String getSchemePriority() 
    {
        return schemePriority;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("schemeId", getSchemeId())
            .append("schemeName", getSchemeName())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .append("schemePriority", getSchemePriority())
            .toString();
    }
}
