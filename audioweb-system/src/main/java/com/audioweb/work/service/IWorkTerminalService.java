package com.audioweb.work.service;

import java.util.List;

import com.audioweb.common.core.domain.Ztree;
import com.audioweb.work.domain.WorkTerminal;

/**
 * 终端管理Service接口
 * 
 * @author shuofang
 * @date 2020-03-01
 */
public interface IWorkTerminalService 
{
    /**
     * 查询终端管理
     * 
     * @param terminalId 终端管理ID
     * @return 终端管理
     */
    public WorkTerminal selectWorkTerminalById(String terRealId);

    /**
     * 查询终端管理列表
     * 
     * @param workTerminal 终端管理
     * @return 终端管理集合
     */
    public List<WorkTerminal> selectWorkTerminalList(WorkTerminal workTerminal);
    /**
     * 查询终端管理列表
     * 
     * @param domainId 分区ID
     * @return 终端管理集合
     */
    public List<WorkTerminal> selectWorkTerminalListByDomId(Long domainId);

    /**
     * 新增终端管理
     * 
     * @param workTerminal 终端管理
     * @return 结果
     */
    public int insertWorkTerminal(WorkTerminal workTerminal);

    /**
     * 修改终端管理
     * 
     * @param workTerminal 终端管理
     * @return 结果
     */
    public int updateWorkTerminal(WorkTerminal workTerminal);
    /**
     * 批量更新终端最后登录时间
     * 
     * @param List<WorkTerminal> 终端管理
     * @return 结果
     */
    public int updateTerminalDateList(List<WorkTerminal> workTerminal);

    /**
     * 批量删除终端管理
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteWorkTerminalByIds(String ids);
    /**
     * 批量更新终端分区
     * 
     * @param ids 需要修改的数据ID
     * @return 结果
     */
    public int updateTerminalDomainByIds(String domainId,String ids);

    /**
     * 删除终端管理信息
     * 
     * @param terminalId 终端管理ID
     * @return 结果
     */
    public int deleteWorkTerminalById(String terRealId);
    /**
     * 校验IP地址是否唯一
     *
     * @param workTerminal 终端信息
     * @return 结果
     */
    public String checkIpUnique(WorkTerminal workTerminal);
    /**
     * 校验终端ID地址是否唯一
     *
     * @param workTerminal 终端信息
     * @return 结果
     */
    public String checkIdUnique(WorkTerminal workTerminal);

	/**
	 * @Title: changeStatus 
	 * @Description: 终端状态修改
	 * @param workTerminal
	 * @return int 返回类型 
	 * @throws 抛出错误
	 * @author ShuoFang 
	 * @date 2020年3月3日 上午11:35:05
	 */
	public int changeStatus(WorkTerminal workTerminal);
	/**
	 * 初始化终端缓存
	 * @Title: initWorkTerminals 
	 * @Description: TODO(这里用一句话描述这个方法的作用)  void 返回类型 
	 * @throws 抛出错误
	 * @author 10155 
	 * @date 2020年3月19日 下午11:28:14
	 */
	public void initWorkTerminals();
	/**
	 * 初始化终端树
	 * @Title: roleTerminalTreeData 
	 * @Description: TODO(这里用一句话描述这个方法的作用)  void 返回类型 
	 * @throws 抛出错误
	 * @author 10155 
	 * @date 2020年3月25日 下午10:00:09
	 */
	public List<Ztree> roleTerminalTreeData(String domainIds,String terIds);
}
