package com.audioweb.work.service.impl;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import com.audioweb.common.utils.DateUtils;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.system.domain.SysDomain;
import com.audioweb.system.service.ISysDomainService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.audioweb.work.domain.WorkTerminal;
import com.audioweb.work.mapper.WorkTerminalMapper;
import com.audioweb.work.service.IWorkTerminalService;
import com.audioweb.common.annotation.DataScope;
import com.audioweb.common.constant.UserConstants;
import com.audioweb.common.constant.WorkConstants;
import com.audioweb.common.core.domain.Ztree;
import com.audioweb.common.core.text.Convert;

/**
 * 终端管理Service业务层处理
 * 
 * @author shuofang
 * @date 2020-03-01
 */
@Service
public class WorkTerminalServiceImpl implements IWorkTerminalService 
{
	private static final String ISNOTCHECK = "0";
	private static final String ISCHECK = "1";
    @Autowired
    private WorkTerminalMapper workTerminalMapper;

    @Autowired
    private ISysDomainService domainService;

    /**
     * 查询终端管理
     * 
     * @param terminalId 终端管理ID
     * @return 终端管理
     */
    @Override
    public WorkTerminal selectWorkTerminalById(String terRealId)
    {
        return workTerminalMapper.selectWorkTerminalById(terRealId);
    }

    /**
     * 查询终端管理列表
     * 
     * @param workTerminal 终端管理
     * @return 终端管理
     */
    @Override
    public List<WorkTerminal> selectWorkTerminalList(WorkTerminal workTerminal)
    {
        return workTerminalMapper.selectWorkTerminalList(workTerminal);
    }

    /**
     * 新增终端管理
     * 
     * @param workTerminal 终端管理
     * @return 结果
     */
    @Override
    public int insertWorkTerminal(WorkTerminal workTerminal)
    {
        workTerminal.setCreateTime(DateUtils.getNowDate());
        workTerminal.put();
        return workTerminalMapper.insertWorkTerminal(workTerminal);
    }

    /**
     * 修改终端管理
     * 
     * @param workTerminal 终端管理
     * @return 结果
     */
    @Override
    public int updateWorkTerminal(WorkTerminal workTerminal)
    {
    	/**缓存同步*/
    	if(WorkTerminal.isExist(workTerminal)) {
    		/**删除停用终端的缓存*/
    		if(StringUtils.isNotEmpty(workTerminal.getStatus()) && !workTerminal.getStatus().equals(WorkConstants.NORMAL)) {
    			workTerminal.remove();
    		}
    	}else {
    		/**添加启用终端的缓存*/
    		if(StringUtils.isNotEmpty(workTerminal.getStatus()) && workTerminal.getStatus().equals(WorkConstants.NORMAL)) {
    			workTerminal.put();
    		}
    	}
        return workTerminalMapper.updateWorkTerminal(workTerminal);
    }

    /**
     * 批量更新终端最后登录时间
     * 
     * @param List<WorkTerminal> 终端管理
     * @return 结果
     */
    public int updateTerminalDateList(List<WorkTerminal> workTerminal) 
    {
    	return workTerminalMapper.updateTerminalDateList(workTerminal);
    }
    /**
     * 删除终端管理对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteWorkTerminalByIds(String ids)
    {
        return workTerminalMapper.deleteWorkTerminalByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除终端管理信息
     * 
     * @param terminalId 终端管理ID
     * @return 结果
     */
    @Override
    public int deleteWorkTerminalById(String terRealId)
    {
        return workTerminalMapper.deleteWorkTerminalById(terRealId);
    }

	/* (non-Javadoc) 
	 * <p>Title: checkPhoneUnique</p> 
	 * <p>Description: </p> 
	 * @author ShuoFang 
	 * @date 2020年3月2日 上午11:04:39
	 * @param workTerminal
	 * @return 
	 * @see com.audioweb.system.service.IWorkTerminalService#checkPhoneUnique(com.audioweb.system.domain.WorkTerminal) 
	 */ 
    /**
     * 校验终端IP地址是否唯一
     *
     * @param workTerminal 终端信息
     * @return
     */
	@Override
	public String checkIpUnique(WorkTerminal workTerminal) {
		try {
			if(!workTerminal.getTerminalIp().equals(InetAddress.getByName(workTerminal.getTerminalIp()).getHostAddress())){
				return WorkConstants.TERMINAL_IP_NOT_UNIQUE;
			}
		} catch (Exception e) {
			return WorkConstants.TERMINAL_IP_NOT_UNIQUE;
		}
		List<WorkTerminal> list = workTerminalMapper.selectWorkTerminalList(workTerminal);
		if(list != null) {
			for(WorkTerminal terminal:list) {
				if(StringUtils.isNotNull(terminal) && !terminal.getDelFlag().equals("2")) {
					if(StringUtils.isNotEmpty(workTerminal.getTerRealId()) && terminal.getTerRealId().equals(workTerminal.getTerRealId())) {
						return WorkConstants.TERMINAL_IP_UNIQUE;
					}else {
						return WorkConstants.TERMINAL_IP_NOT_UNIQUE;
					}
				}
			}
		}
        return WorkConstants.TERMINAL_IP_UNIQUE;
	}

	/* (non-Javadoc) 
	 * <p>Title: checkIdUnique</p> 
	 * <p>Description: </p> 
	 * @author ShuoFang 
	 * @date 2020年3月3日 上午9:14:18
	 * @param workTerminal
	 * @return 
	 * @see com.audioweb.work.service.IWorkTerminalService#checkIdUnique(com.audioweb.work.domain.WorkTerminal) 
	 */ 
	/**
     * 校验终端ID是否唯一
     *
     * @param workTerminal 终端信息
     * @return
     */
	@Override
	public String checkIdUnique(WorkTerminal workTerminal) {
		try {
			int id = Integer.parseInt(workTerminal.getTerminalId());
			if(id < 0 && id > 9999 ){
				return WorkConstants.TERMINAL_ID_NOT_UNIQUE;
			}
		} catch (Exception e) {
			return WorkConstants.TERMINAL_ID_NOT_UNIQUE;
		}
		List<WorkTerminal> list = workTerminalMapper.selectWorkTerminalList(workTerminal);
		if(list != null) {
			for(WorkTerminal terminal:list) {
				if(StringUtils.isNotNull(terminal) && !terminal.getDelFlag().equals("2")) {
					if(StringUtils.isNotEmpty(workTerminal.getTerRealId()) && terminal.getTerRealId().equals(workTerminal.getTerRealId())) {
						return WorkConstants.TERMINAL_IP_UNIQUE;
					}else {
						return WorkConstants.TERMINAL_ID_NOT_UNIQUE;
					}
				}
			}
		}
		return WorkConstants.TERMINAL_ID_UNIQUE;
	}

	/* (non-Javadoc) 
	 * <p>Title: changeStatus</p> 
	 * <p>Description: </p> 
	 * @author ShuoFang 
	 * @date 2020年3月3日 上午11:36:03
	 * @param workTerminal
	 * @return 
	 * @see com.audioweb.work.service.IWorkTerminalService#changeStatus(com.audioweb.work.domain.WorkTerminal) 
	 */ 
	/**
     * 终端状态修改
     *
     * @param workTerminal 终端信息
     * @return
     */
	@Override
	public int changeStatus(WorkTerminal workTerminal) 
	{
		/**缓存同步*/
		WorkTerminal terminal = WorkTerminal.getTerById(workTerminal);
		if(StringUtils.isNotNull(terminal)) {
			if(!workTerminal.getStatus().equals(WorkConstants.NORMAL)) {
				terminal.remove();
			}
		}else if(workTerminal.getStatus().equals(WorkConstants.NORMAL)){
			terminal = workTerminalMapper.selectWorkTerminalById(workTerminal.getTerRealId());
			terminal.setStatus(workTerminal.getStatus());
			terminal.put();
		}
		return workTerminalMapper.updateWorkTerminal(workTerminal);
	}

	/* (non-Javadoc) 
	 * <p>Title: updateTerminalDomainByIds</p> 
	 * <p>Description: </p> 
	 * @author 10155 
	 * @date 2020年3月17日 下午6:07:16
	 * @param domainId
	 * @param ids
	 * @return 
	 * @see com.audioweb.work.service.IWorkTerminalService#updateTerminalDomainByIds(java.lang.String, java.lang.String) 
	 */ 
	 /**
     * 批量修改终端分区
     * 
     * @param ids 需要修改的数据ID
     * @return 结果
     */
	@Override
	public int updateTerminalDomainByIds(String domainId, String ids) 
	{
		HashMap<String, Object> map = new HashMap<>();
		map.put("domainId", domainId);
		map.put("ids", Convert.toStrArray(ids));
		return workTerminalMapper.updateTerminalDomainByIds(map);
	}

	@Override
	public void initWorkTerminals() {
		WorkTerminal terminal = new WorkTerminal();
		terminal.getDomain().setStatus(UserConstants.DOMAIN_NORMAL);
		terminal.setStatus(WorkConstants.NORMAL);
		terminal.clear();
		List<WorkTerminal> workTerminal  = workTerminalMapper.selectWorkTerminalList(terminal);
		for(WorkTerminal wTerminal:workTerminal) {
			wTerminal.put();
		}
	}
	/***
	 * 初始化终端复选树
	 */
	@Override
	public List<Ztree> roleTerminalTreeData(String domainIds,String terIds) {
		List<String> doms = Convert.strToList(domainIds);
		List<String> ters = Convert.strToList(terIds);
		List<SysDomain> father =domainService.selectDomainList(new SysDomain());
		List<Ztree> result = new ArrayList<Ztree>();
		for(SysDomain domain:father) {
			/**分区正常*/
			if(UserConstants.DOMAIN_NORMAL.equals(domain.getStatus())) {
                Ztree ztree = new Ztree();
                ztree.setId(domain.getDomainId());
                ztree.setpId(domain.getParentId());
                ztree.setName(domain.getDomainName());
                ztree.setTitle(domain.getDomainName());
                for(String dom:doms) {
                	if(dom.contains(String.valueOf(domain.getDomainId()))) {
                		/**可能存在相同的ID*/
                		ztree.setChecked(true);
                		result.add(ztree);
                		if(dom.contains("_")) {
                			/**存在下划线为半选,需要进一步筛选*/
                			result.addAll(getChildren(domain,ters,ISNOTCHECK));
                		}else {
                			result.addAll(getChildren(domain,ters,ISCHECK));
                		}
                	}else {
                		/**未被选中*/
                		result.addAll(getChildren(domain,ters,ISNOTCHECK));
                	}
                }
			}
		}
		return result;
	}
	/**
	 * 
	 * @Title: getChildren 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param domain
	 * @param terIds
	 * @param checked 0 为半选或未选,1为全选
	 * @return List<Ztree> 返回类型 
	 * @throws 抛出错误
	 * @author 10155 
	 * @date 2020年3月25日 下午10:59:38
	 */
	private List<Ztree> getChildren(SysDomain domain,List<String> terIds,String checked) {
		List<Ztree> child = new ArrayList<Ztree>();
		WorkTerminal terminal = new WorkTerminal();
		terminal.setStatus(WorkConstants.NORMAL);
		terminal.setDomain(domain);
		/**获取当前分区下全部*/
		List<WorkTerminal> terminals = workTerminalMapper.selectWorkTerminalList(terminal);
		for(WorkTerminal ter:terminals) {
			Ztree tree = new Ztree();
			tree.setpId(ter.getDomainId());
			tree.setId(Long.parseLong(ter.getTerRealId()));
			tree.setName(ter.getTerminalName());
			tree.setTitle(ter.getTerminalId());
			tree.setIconSkin("icon02");
			if("0".equals(checked)) {//非全选
				if(terIds.contains(ter.getTerRealId())) {
					tree.setChecked(true);
					terIds.remove(ter.getTerRealId());
				}
			}else{
				tree.setChecked(true);
			}
			child.add(tree);
		}
		return child;
	}
}
