package com.audioweb.work.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.audioweb.work.mapper.WorkSchemeTaskMapper;
import com.audioweb.work.domain.WorkSchemeTask;
import com.audioweb.work.service.IWorkSchemeTaskService;
import com.audioweb.common.core.text.Convert;

/**
 * 定时任务Service业务层处理
 * 
 * @author shuofang
 * @date 2020-05-09
 */
@Service
public class WorkSchemeTaskServiceImpl implements IWorkSchemeTaskService 
{
    @Autowired
    private WorkSchemeTaskMapper workSchemeTaskMapper;

    /**
     * 查询定时任务
     * 
     * @param schemeTaskId 定时任务ID
     * @return 定时任务
     */
    @Override
    public WorkSchemeTask selectWorkSchemeTaskById(Long schemeTaskId)
    {
        return workSchemeTaskMapper.selectWorkSchemeTaskById(schemeTaskId);
    }

    /**
     * 查询定时任务列表
     * 
     * @param workSchemeTask 定时任务
     * @return 定时任务
     */
    @Override
    public List<WorkSchemeTask> selectWorkSchemeTaskList(WorkSchemeTask workSchemeTask)
    {
        return workSchemeTaskMapper.selectWorkSchemeTaskList(workSchemeTask);
    }

    /**
     * 新增定时任务
     * 
     * @param workSchemeTask 定时任务
     * @return 结果
     */
    @Override
    public int insertWorkSchemeTask(WorkSchemeTask workSchemeTask)
    {
        return workSchemeTaskMapper.insertWorkSchemeTask(workSchemeTask);
    }

    /**
     * 修改定时任务
     * 
     * @param workSchemeTask 定时任务
     * @return 结果
     */
    @Override
    public int updateWorkSchemeTask(WorkSchemeTask workSchemeTask)
    {
        return workSchemeTaskMapper.updateWorkSchemeTask(workSchemeTask);
    }

    /**
     * 删除定时任务对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteWorkSchemeTaskByIds(String ids)
    {
        return workSchemeTaskMapper.deleteWorkSchemeTaskByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除定时任务信息
     * 
     * @param schemeTaskId 定时任务ID
     * @return 结果
     */
    @Override
    public int deleteWorkSchemeTaskById(Long schemeTaskId)
    {
        return workSchemeTaskMapper.deleteWorkSchemeTaskById(schemeTaskId);
    }
}