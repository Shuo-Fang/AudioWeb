package com.audioweb.work.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.audioweb.common.annotation.Excel;
import com.audioweb.common.annotation.Excels;
import com.audioweb.common.annotation.Excel.Type;
import com.audioweb.common.core.domain.BaseEntity;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.system.domain.SysDomain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.validation.constraints.Size;

/**
 * 终端管理对象 work_terminal
 * 
 * @author shuofang
 * @date 2020-03-01
 */
public class WorkTerminal extends BaseEntity implements BaseWork
{
	private static final long serialVersionUID = 1L;
	/** 181 = 240*0.75+1 设定预定满载值为240*/
	private static Map<String, WorkTerminal> terminalMap = new ConcurrentHashMap<String, WorkTerminal>();

    /** 终端序号ID */
    private String terRealId;
    
    
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
    private String isAutoCast;

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

    /** 分区对象 */
    @Excels({
        @Excel(name = "分区名称", targetAttr = "domainName", type = Type.EXPORT),
        @Excel(name = "分区负责人", targetAttr = "leader", type = Type.EXPORT)
    })
    private SysDomain domain;
    
    /** 广播信息 */
    private WorkCastTask castTask; 
    
    /** 是否在线	0为在线(刚刚通信过),1为在线(即将离线),2为离线*/
    private Integer isOnline; 
    public WorkTerminal() {
    	
	}
    public WorkTerminal(String id) {
    	terminalId = id;
    }
    
    public String getTerRealId() {
		return terRealId;
	}
	public void setTerRealId(String terRealId) {
		this.terRealId = terRealId;
	}
	public void setTerminalId(String terminalId) 
    {
        this.terminalId = terminalId;
    }
    @Size(min = 0, max = 4, message = "终端ID长度不能超过4个字符")
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
    @Size(min = 0, max = 30, message = "IP地址出错")
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
	public String getIsAutoCast() {
		return isAutoCast;
	}
	public void setIsAutoCast(String isAutoCast) {
		this.isAutoCast = isAutoCast;
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

    public SysDomain getDomain() {
    	if (domain == null)
        {
            domain = new SysDomain();
        }
        return domain;
	}

	public void setDomain(SysDomain domain) {
		this.domain = domain;
	}
	public WorkCastTask getCastTask() {
		return castTask;
	}
	public void setCastTask(WorkCastTask castTask) {
		this.castTask = castTask;
	}
	public Integer getIsOnline() {
		return isOnline;
	}
	public void setIsOnline(Integer isOnline) {
		this.isOnline = isOnline;
	}
	public static Map<String, WorkTerminal> getTerminalMap() {
		return terminalMap;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 17;
		result = prime * result + ((terminalId == null) ? 0 : terminalId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WorkTerminal other = (WorkTerminal) obj;
		if (terminalId == null) {
			if (other.terminalId != null)
				return false;
		} else if (!terminalId.equals(other.terminalId))
			return false;
		return true;
	}
	
	/** 将终端信息存入维护 */
	@Override
	public boolean put() {
		if(StringUtils.isNotEmpty(terminalIp)) {
			terminalMap.put(terminalIp, this);
			return true;
		}else {
			return false;
		}
	}
	
	/** 查询终端信息是否存在  */
	@Override
	public boolean exist() {
		return terminalMap.containsKey(terminalIp);
	}
	
	/**获取维护的指定终端信息*/
	@Override
	public WorkTerminal get() {
		if(StringUtils.isNotEmpty(terminalIp)) {
			return terminalMap.get(terminalIp);
		}else {
			return null;
		}
	}
	
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("terminalId", getTerminalId())
            .append("terminalName", getTerminalName())
            .append("terminalIp", getTerminalIp())
            .append("isCmic", getIsCmic())
            .append("isAutoCast", getIsAutoCast())
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
	@Override
	public void clear() {
		terminalMap.clear();
	}
	
	@Override
	public List<WorkTerminal> export() {
		return new ArrayList<WorkTerminal>(terminalMap.values());
	}
	
	/**将全部的对象更新替换为缓存中存储的对象**/
	public static void loadAll(List<WorkTerminal> entity) {
		for(WorkTerminal task : entity) {
			if(task.exist()) {
				task = task.get();
			}
		}
	}
	
	@Override
	public boolean remove() {
		return StringUtils.isNotNull(terminalMap.remove(terminalIp));
	}
	
	/**通过realID查询终端是否缓存*/
	public static boolean isExist(WorkTerminal workTerminal) {
		ArrayList<WorkTerminal> terminals = new ArrayList<WorkTerminal>(terminalMap.values());
		for(WorkTerminal t:terminals) {
			if(t.getTerRealId().equals(workTerminal.getTerRealId())) {
				return true;
			}
		}
		return false;
	}
	/**通过realID查询终端实际存储信息*/
	public static WorkTerminal getTerById(WorkTerminal workTerminal) {
		ArrayList<WorkTerminal> terminals = new ArrayList<WorkTerminal>(terminalMap.values());
		for(WorkTerminal t:terminals) {
			if(t.getTerRealId().equals(workTerminal.getTerRealId())) {
				return t;
			}
		}
		return null;
	}
	/**通过ip查询终端实际存储信息*/
	public static WorkTerminal getTerByIp(String ip) {
		return terminalMap.get(ip);
	}
}
