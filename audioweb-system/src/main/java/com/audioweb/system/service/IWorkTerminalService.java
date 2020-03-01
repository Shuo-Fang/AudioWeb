package com.audioweb.system.service;

import com.audioweb.system.domain.WorkTerminal;
import java.util.List;

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
    public WorkTerminal selectWorkTerminalById(String terminalId);

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
     * 删除终端管理信息
     * 
     * @param terminalId 终端管理ID
     * @return 结果
     */
    public int deleteWorkTerminalById(String terminalId);
}
