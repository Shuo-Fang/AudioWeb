package com.audioweb.system.service.impl;

import java.util.List;
import com.audioweb.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.audioweb.system.mapper.WorkTerminalMapper;
import com.audioweb.system.domain.WorkTerminal;
import com.audioweb.system.service.IWorkTerminalService;
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
    @Autowired
    private WorkTerminalMapper workTerminalMapper;

    /**
     * 查询终端管理
     * 
     * @param terminalId 终端管理ID
     * @return 终端管理
     */
    @Override
    public WorkTerminal selectWorkTerminalById(String terminalId)
    {
        return workTerminalMapper.selectWorkTerminalById(terminalId);
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
        return workTerminalMapper.updateWorkTerminal(workTerminal);
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
    public int deleteWorkTerminalById(String terminalId)
    {
        return workTerminalMapper.deleteWorkTerminalById(terminalId);
    }
}
