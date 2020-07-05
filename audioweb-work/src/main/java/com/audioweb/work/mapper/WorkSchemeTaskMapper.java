package com.audioweb.work.mapper;

import java.util.List;
import com.audioweb.work.domain.WorkSchemeTask;

/**
 * 定时任务Mapper接口
 * 
 * @author shuofang
 * @date 2020-05-09
 */
public interface WorkSchemeTaskMapper 
{
    /**
     * 查询定时任务
     * 
     * @param schemeTaskId 定时任务ID
     * @return 定时任务
     */
    public WorkSchemeTask selectWorkSchemeTaskById(Long schemeTaskId);
    
    /**
     * 根据ID批量查询定时任务
     * 
     * @param schemeTaskId 定时任务ID
     * @return 定时任务
     */
    public List<WorkSchemeTask> selectWorkSchemeTaskByIds(String[] schemeTaskIds);

    /**
     * 查询定时任务列表
     * 
     * @param workSchemeTask 定时任务
     * @return 定时任务集合
     */
    public List<WorkSchemeTask> selectWorkSchemeTaskList(WorkSchemeTask workSchemeTask);

    /**
     * 新增定时任务
     * 
     * @param workSchemeTask 定时任务
     * @return 结果
     */
    public int insertWorkSchemeTask(WorkSchemeTask workSchemeTask);

    /**
     * 修改定时任务
     * 
     * @param workSchemeTask 定时任务
     * @return 结果
     */
    public int updateWorkSchemeTask(WorkSchemeTask workSchemeTask);

    /**
     * 删除定时任务
     * 
     * @param schemeTaskId 定时任务ID
     * @return 结果
     */
    public int deleteWorkSchemeTaskById(Long schemeTaskId);

    /**
     * 批量删除定时任务
     * 
     * @param schemeTaskIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteWorkSchemeTaskByIds(String[] schemeTaskIds);
}