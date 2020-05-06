/**   
 * @Title: CastTask.java 
 * @Package com.audioweb.system.domain 
 * @Description: 广播任务信息
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com
 * @date 2020年3月2日 下午1:27:26 
 * @version V1.0   
 */ 
package com.audioweb.work.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.audioweb.common.core.domain.BaseEntity;
import com.audioweb.common.enums.CastWorkType;
import com.audioweb.common.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/** 
 * @ClassName: CastTask 
 * @Description: 广播任务信息
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2020年3月2日 下午1:27:26  
 */
@ApiModel("广播任务实体")
@JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL) 
public class WorkCastTask extends BaseEntity implements BaseWork,Comparable<WorkCastTask> {
	private static volatile AtomicLong atomicLong = new AtomicLong(0);
	
	private static final long serialVersionUID = 1L;
	/**默认16宽度即可，一般达不到满载*/
	protected static Map<Long, WorkCastTask> taskMap = new ConcurrentHashMap<Long, WorkCastTask>();

	/** 广播编号ID */
	@ApiModelProperty("广播编号ID")
	protected Long taskId = atomicLong.incrementAndGet();
	
	/** 广播任务名称 */
	@ApiModelProperty("广播任务名称")
	private String taskName;
	
	/** 广播音量 */
	@ApiModelProperty("广播音量")
	private Integer vol;
	
	/**	广播类型 */
	@ApiModelProperty("广播类型:0,文件广播;1,定时广播;2,实时采播;3,终端采播;4,终端点播;5,寻呼话筒;6,控件广播;7,文本广播")
	private CastWorkType castType;
	
	/**	广播地址 */
	@ApiModelProperty("广播地址")
	@JsonIgnore
	private String castAddress;
	
	/**	广播端口 */
	@ApiModelProperty("广播端口")
	@JsonIgnore
	private Integer castPort;
	
	/**	广播级别 */
	@ApiModelProperty("广播级别")
	private Integer castLevel;
	
    /** 备注 */
	@ApiModelProperty("备注")
	private String remark;
    
    /** 是否正在广播 */
	@ApiModelProperty("是否正在广播")
	private volatile boolean isCast = false;
	
	/** 是否暂停广播 */
	@ApiModelProperty("是否暂停广播")
	private boolean isStop = false;

    /**	广播初始化分区列表 */
	@ApiModelProperty("广播初始化分区列表，逗号分隔，未全选分区id前带_")
	private String domainIdList;
	
	/**	广播初始化分区列表 */
	@ApiModelProperty("广播初始化终端列表，id为realId，逗号分隔")
	private String terIdList;
	
	/**	广播初始化终端列表 */
	@ApiModelProperty("广播初始化终端列表")
	@JsonIgnore
	private List<WorkTerminal> castTeridList;

	/**	正在广播终端列表 */
/*	@ApiModelProperty("正在广播终端列表")
	@JsonIgnore
	private List<WorkTerminal> castlist;*/
	
	@Override
	public boolean put() {
		if(StringUtils.isNotNull(taskId)) {
			taskMap.put(taskId, this);
			return true;
		}else {
			return false;
		}
	}
	@Override
	public boolean exist() {
		return taskMap.containsKey(taskId);
	}
	@Override
	public WorkCastTask get() {
		if(StringUtils.isNotNull(taskId)) {
			return taskMap.get(taskId);
		}else {
			return null;
		}
	}
	@Override
	public void clear() {
		taskMap.clear();
	}
	
	@Override
	public List<WorkCastTask> export() {
		List<WorkCastTask> returnResult = new ArrayList<WorkCastTask>();
        Set<Entry<Long, WorkCastTask>> eSet  =  taskMap.entrySet();
        Iterator<Entry<Long, WorkCastTask>> it = eSet.iterator();
        while(it.hasNext()) {
            returnResult.add(it.next().getValue());
        }
		return returnResult;
	}
	
	/**将全部的对象更新替换为缓存中存储的对象**/
	public static void loadAll(List<WorkCastTask> entity) {
		for(WorkCastTask task : entity) {
			if(task.exist()) {
				task = task.get();
			}
		}
	}
	public static WorkCastTask find(Long taskId) {
		if(StringUtils.isNotNull(taskId)) {
			return taskMap.get(taskId);
		}else {
			return null;
		}
	}
	public static WorkCastTask removeByTaskId(Long taskId) {
		if(StringUtils.isNotNull(taskId)) {
			return taskMap.remove(taskId);
		}else {
			return null;
		}
	}
	@Override
	public boolean remove() {
		return StringUtils.isNotNull(taskMap.remove(taskId));
	}
	
    public Long getTaskId() {
		return taskId;
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
	@Override
	public String getRemark() {
		return remark;
	}
	@Override
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public List<WorkTerminal> getCastTeridList() {
		return castTeridList;
	}
	public void setCastTeridList(List<WorkTerminal> castTeridList) {
		this.castTeridList = castTeridList;
	}
	public String getDomainIdList() {
		return domainIdList;
	}

	public void setDomainIdList(String domainIdList) {
		this.domainIdList = domainIdList;
	}
	public String getTerIdList() {
		return terIdList;
	}

	public void setTerIdList(String terIdList) {
		this.terIdList = terIdList;
	}

	public boolean getIsCast() {
		return isCast;
	}
	public void setIsCast(boolean isCast) {
		this.isCast = isCast;
	}
	public boolean getIsStop() {
		return isStop;
	}
	public void setIsStop(boolean isStop) {
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
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		WorkCastTask other = (WorkCastTask) obj;
		if (taskId == null) {
			if (other.taskId != null) {
				return false;
			}
		} else if (!taskId.equals(other.taskId)) {
			return false;
		}
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
	
	/**重写Comparable接口的compareTo方法*/
	@Override
	public int compareTo(WorkCastTask task) {           
		try {
			// 根据castLevel升序排列，降序修改相减顺序即可
			return this.castLevel - task.castLevel;
		} catch (Exception e) {
			return 0;
		}
	}
}
