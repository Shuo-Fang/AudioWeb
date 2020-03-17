package com.audioweb.work.service;

import java.util.List;

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
}
