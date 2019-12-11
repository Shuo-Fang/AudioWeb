package com.audioweb.clientserver.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.audioweb.common.annotation.Excel;
import com.audioweb.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 终端对象 work_terminal
 * 
 * @author shuofang
 * @date 2019-12-11
 */
public class WorkTerminal extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 终端ID */
    private Long terminalId;

    /** 终端名称 */
    @Excel(name = "终端名称")
    private String terminalName;

    /** 终端IP地址 */
    @Excel(name = "终端IP地址")
    private String terminalIp;

    /** 自动是否允许寻呼话筒 */
    @Excel(name = "自动是否允许寻呼话筒")
    private String iscmic;

    /** 终端是否允许自动采播 */
    @Excel(name = "终端是否允许自动采播")
    private String isautocast;

    /** 所在分区ID */
    private Long domainId;

    /** 终端状态（0正常 1停用） */
    @Excel(name = "终端状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 终端最后登录时间 */
    @Excel(name = "终端最后登录时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date loginTime;

    /** 终端管理分区 */
    private Long precinct;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setTerminalId(Long terminalId) 
    {
        this.terminalId = terminalId;
    }

    public Long getTerminalId() 
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
    public void setIscmic(String iscmic) 
    {
        this.iscmic = iscmic;
    }

    public String getIscmic() 
    {
        return iscmic;
    }
    public void setIsautocast(String isautocast) 
    {
        this.isautocast = isautocast;
    }

    public String getIsautocast() 
    {
        return isautocast;
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
    public void setPrecinct(Long precinct) 
    {
        this.precinct = precinct;
    }

    public Long getPrecinct() 
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
            .append("iscmic", getIscmic())
            .append("isautocast", getIsautocast())
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
