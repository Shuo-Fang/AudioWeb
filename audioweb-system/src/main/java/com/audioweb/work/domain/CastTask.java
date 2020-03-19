/**   
 * @Title: CastTask.java 
 * @Package com.audioweb.system.domain 
 * @Description: 广播任务信息
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com
 * @date 2020年3月2日 下午1:27:26 
 * @version V1.0   
 */ 
package com.audioweb.work.domain;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.audioweb.common.core.domain.BaseEntity;
import com.audioweb.common.enums.CastWorkType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/** 
 * @ClassName: CastTask 
 * @Description: 广播任务信息
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2020年3月2日 下午1:27:26  
 */
@ApiModel("广播任务实体")
public class CastTask extends BaseEntity{
	public static final String FILECASTTASK = "FileCastTask";
	private static final long serialVersionUID = 1L;
	
	/** 广播编号ID */
	@ApiModelProperty("广播编号ID")
	private Long taskId;
	
	/** 广播任务名称 */
	@ApiModelProperty("广播任务名称")
	private String taskName;
	
	/** 广播音量 */
	@ApiModelProperty("广播音量")
	private Integer vol = -1;
	
	/**	广播类型 */
	@ApiModelProperty("广播类型:0,文件广播;1,定时广播;2,实时采播;3,终端采播;4,终端点播;5,寻呼话筒;6,控件广播;7,文本广播")
	private CastWorkType castType;
	
	/**	广播地址 */
	@ApiModelProperty("广播地址")
	private String castAddress;
	
	/**	广播端口 */
	@ApiModelProperty("广播端口")
	private Integer castPort;
	
	/**	广播级别 */
	@ApiModelProperty("广播级别")
	private Integer castLevel;
	
    /** 备注 */
	@ApiModelProperty("备注")
	private String remark;
    
    /** 是否正在广播 */
	@ApiModelProperty("是否正在广播")
	private Boolean isCast = false;
	
	/** 是否暂停广播 */
	@ApiModelProperty("是否暂停广播")
	private Boolean isStop;

    /**	广播初始化分区列表 */
	@ApiModelProperty("广播初始化分区列表")
	private List<String> domainidlist;
	
	/**	广播初始化终端列表 */
	@ApiModelProperty("广播初始化终端列表")
	private List<WorkTerminal> castTeridlist;

	/**	正在广播终端列表 */
	@ApiModelProperty("正在广播终端列表")
	private List<WorkTerminal> castlist;
	
    
    public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public Integer getVol() {
		return vol;
	}
	public void setVol(Integer vol) {
		this.vol = vol;
	}
	public CastWorkType getCastType() {
		return castType;
	}
	public void setCastType(CastWorkType castType) {
		this.castType = castType;
	}
	public String getCastAddress() {
		return castAddress;
	}
	public void setCastAddress(String castAddress) {
		this.castAddress = castAddress;
	}
	public Integer getCastPort() {
		return castPort;
	}
	public void setCastPort(Integer castPort) {
		this.castPort = castPort;
	}
	public Integer getCastLevel() {
		return castLevel;
	}
	public void setCastLevel(Integer castLevel) {
		this.castLevel = castLevel;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
    public List<String> getDomainidlist() {
		return domainidlist;
	}
	public void setDomainidlist(List<String> domainidlist) {
		this.domainidlist = domainidlist;
	}
	public List<WorkTerminal> getCastTeridlist() {
		return castTeridlist;
	}
	public void setCastTeridlist(List<WorkTerminal> castTeridlist) {
		this.castTeridlist = castTeridlist;
	}
	public List<WorkTerminal> getCastlist() {
		return castlist;
	}
	public void setCastlist(List<WorkTerminal> castlist) {
		this.castlist = castlist;
	}
	public Boolean getIsCast() {
		return isCast;
	}
	public void setIsCast(Boolean isCast) {
		this.isCast = isCast;
	}
	public Boolean getIsStop() {
		return isStop;
	}
	public void setIsStop(Boolean isStop) {
		this.isStop = isStop;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 17;
		result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
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
		CastTask other = (CastTask) obj;
		if (taskId == null) {
			if (other.taskId != null)
				return false;
		} else if (!taskId.equals(other.taskId))
			return false;
		return true;
	}
	
	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("taskId", getTaskId())
            .append("taskName", getTaskName())
            .append("vol", getVol())
            .append("castType", getCastType())
            .append("castAddress", getCastAddress())
            .append("castPort", getCastPort())
            .append("castLevel", getCastLevel())
            .append("remark", getRemark())
            .toString();
    }
}
