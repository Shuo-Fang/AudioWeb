package com.audioweb.work.mapper;

import com.audioweb.work.domain.WorkTerminal;

import java.util.HashMap;
import java.util.List;

/**
 * 终端管理Mapper接口
 * 
 * @author shuofang
 * @date 2020-03-01
 */
public interface WorkTerminalMapper 
{
    /**
     * 查询终端管理
     * 
     * @param terminalId 终端管理ID
     * @return 终端管理
     */
    public WorkTerminal selectWorkTerminalById(String terminalId);

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
     * @param domainId 终端管理
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
     * 修改终端在线状态管理
     * 
     * @param workTerminal 终端管理
     * @return 结果
     */
    public int updateTerminalOnline(WorkTerminal workTerminal);
    /**
     * 批量更新终端最后登录时间
     * 
     * @param List<WorkTerminal> 终端管理
     * @return 结果
     */
    public int updateTerminalDateList(List<WorkTerminal> workTerminal);

    /**
     * 删除终端管理
     * 
     * @param terminalId 终端管理ID
     * @return 结果
     */
    public int deleteWorkTerminalById(String terminalId);

    /**
     * 批量删除终端管理
     * 
     * @param terminalIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteWorkTerminalByIds(String[] terminalIds);
    /**
     * 批量修改终端分区
     * 
     * @param terminalIds 需要修改的数据ID
     * @return 结果
     */
    public int updateTerminalDomainByIds(HashMap<String, Object> map);
}
