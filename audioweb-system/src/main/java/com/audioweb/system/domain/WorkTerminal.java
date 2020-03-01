package com.audioweb.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.audioweb.common.annotation.Excel;
import com.audioweb.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 终端管理对象 work_terminal
 * 
 * @author shuofang
 * @date 2020-03-01
 */
public class WorkTerminal extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 终端ID */
    private String terminalId;

    /** 终端名称 */
    @Excel(name = "终端名称")
    private String terminalName;

    /** IP */
    @Excel(name = "IP")
    private String terminalIp;

    /** 寻呼话筒 */
    @Excel(name = "寻呼话筒")
    private String isCmic;

    /** 自动采播 */
    @Excel(name = "自动采播")
    private String isAotoCast;

    /** 所在分区 */
    @Excel(name = "所在分区")
    private Long domainId;

    /** 终端状态（0正常 1停用） */
    @Excel(name = "终端状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 终端最后登录时间 */
    @Excel(name = "终端最后登录时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date loginTime;

    /** 终端管理分区 */
    private String precinct;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setTerminalId(String terminalId) 
    {
        this.terminalId = terminalId;
    }

    public String getTerminalId() 
    {
        return terminalId;
    }
    public void setTerminalName(String terminalName) 
    {
        this.terminalName = terminalName;
    }

    public String getTerminalName() 
    {
        return terminalName;
    }
    public void setTerminalIp(String terminalIp) 
    {
        this.terminalIp = terminalIp;
    }

    public String getTerminalIp() 
    {
        return terminalIp;
    }
    public void setIsCmic(String isCmic) 
    {
        this.isCmic = isCmic;
    }

    public String getIsCmic() 
    {
        return isCmic;
    }
    public void setIsAotoCast(String isAotoCast) 
    {
        this.isAotoCast = isAotoCast;
    }

    public String getIsAotoCast() 
    {
        return isAotoCast;
    }
    public void setDomainId(Long domainId) 
    {
        this.domainId = domainId;
    }

    public Long getDomainId() 
    {
        return domainId;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setLoginTime(Date loginTime) 
    {
        this.loginTime = loginTime;
    }

    public Date getLoginTime() 
    {
        return loginTime;
    }
    public void setPrecinct(String precinct) 
    {
        this.precinct = precinct;
    }

    public String getPrecinct() 
    {
        return precinct;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("terminalId", getTerminalId())
            .append("terminalName", getTerminalName())
            .append("terminalIp", getTerminalIp())
            .append("isCmic", getIsCmic())
            .append("isAotoCast", getIsAotoCast())
            .append("domainId", getDomainId())
            .append("status", getStatus())
            .append("loginTime", getLoginTime())
            .append("precinct", getPrecinct())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("remark", getRemark())
            .toString();
    }
}
