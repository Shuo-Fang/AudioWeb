package com.audioweb.clientserver.service.impl;

import java.util.List;
import com.audioweb.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.audioweb.clientserver.mapper.WorkTerminalMapper;
import com.audioweb.clientserver.domain.WorkTerminal;
import com.audioweb.clientserver.service.IWorkTerminalService;
import com.audioweb.common.core.text.Convert;

/**
 * 终端Service业务层处理
 * 
 * @author shuofang
 * @date 2019-12-11
 */
@Service
public class WorkTerminalServiceImpl implements IWorkTerminalService 
{
    @Autowired
    private WorkTerminalMapper workTerminalMapper;

    /**
     * 查询终端
     * 
     * @param terminalId 终端ID
     * @return 终端
     */
    @Override
    public WorkTerminal selectWorkTerminalById(Long terminalId)
    {
        return workTerminalMapper.selectWorkTerminalById(terminalId);
    }

    /**
     * 查询终端列表
     * 
     * @param workTerminal 终端
     * @return 终端
     */
    @Override
    public List<WorkTerminal> selectWorkTerminalList(WorkTerminal workTerminal)
    {
        return workTerminalMapper.selectWorkTerminalList(workTerminal);
    }

    /**
     * 新增终端
     * 
     * @param workTerminal 终端
     * @return 结果
     */
    @Override
    public int insertWorkTerminal(WorkTerminal workTerminal)
    {
        workTerminal.setCreateTime(DateUtils.getNowDate());
        return workTerminalMapper.insertWorkTerminal(workTerminal);
    }

    /**
     * 修改终端
     * 
     * @param workTerminal 终端
     * @return 结果
     */
    @Override
    public int updateWorkTerminal(WorkTerminal workTerminal)
    {
        return workTerminalMapper.updateWorkTerminal(workTerminal);
    }

    /**
     * 删除终端对象
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
     * 删除终端信息
     * 
     * @param terminalId 终端ID
     * @return 结果
     */
    @Override
    public int deleteWorkTerminalById(Long terminalId)
    {
        return workTerminalMapper.deleteWorkTerminalById(terminalId);
    }
}
