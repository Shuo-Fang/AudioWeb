package com.audioweb.clientserver.mapper;

import com.audioweb.clientserver.domain.WorkTerminal;
import java.util.List;

/**
 * 终端Mapper接口
 * 
 * @author shuofang
 * @date 2019-12-11
 */
public interface WorkTerminalMapper 
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
     * 删除终端
     * 
     * @param terminalId 终端ID
     * @return 结果
     */
    public int deleteWorkTerminalById(Long terminalId);

    /**
     * 批量删除终端
     * 
     * @param terminalIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteWorkTerminalByIds(String[] terminalIds);
}
