package com.audioweb.work.service.impl;

import java.net.InetAddress;
import java.util.List;
import com.audioweb.common.utils.DateUtils;
import com.audioweb.common.utils.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.audioweb.work.domain.WorkTerminal;
import com.audioweb.work.mapper.WorkTerminalMapper;
import com.audioweb.work.service.IWorkTerminalService;
import com.audioweb.common.constant.WorkConstants;
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
		// TODO Auto-generated method stub
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
		return workTerminalMapper.updateWorkTerminal(workTerminal);
	}
}
