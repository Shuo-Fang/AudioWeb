package com.audioweb.clientserver.service;

import com.audioweb.clientserver.domain.WorkTerminal;
import java.util.List;

/**
 * 终端Service接口
 * 
 * @author shuofang
 * @date 2019-12-11
 */
public interface IWorkTerminalService 
{
    /**
     * 查询终端
     * 
     * @param terminalId 终端ID
     * @return 终端
     */
    public WorkTerminal selectWorkTerminalById(Long terminalId);

    /**
     * 查询终端列表
     * 
     * @param workTerminal 终端
     * @return 终端集合
     */
    public List<WorkTerminal> selectWorkTerminalList(WorkTerminal workTerminal);

    /**
     * 新增终端
     * 
     * @param workTerminal 终端
     * @return 结果
     */
    public int insertWorkTerminal(WorkTerminal workTerminal);

    /**
     * 修改终端
     * 
     * @param workTerminal 终端
     * @return 结果
     */
    public int updateWorkTerminal(WorkTerminal workTerminal);

    /**
     * 批量删除终端
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteWorkTerminalByIds(String ids);

    /**
     * 删除终端信息
     * 
     * @param terminalId 终端ID
     * @return 结果
     */
    public int deleteWorkTerminalById(Long terminalId);
}
