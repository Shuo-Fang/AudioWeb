package com.audioweb.work.mapper;

import com.audioweb.work.domain.WorkCastTask;
import java.util.List;

/**
 * 广播任务Mapper接口
 * 
 * @author shuofang
 * @date 2020-03-21
 */
public interface WorkCastTaskMapper 
{
    /**
     * 查询广播任务
     * 
     * @param taskId 广播任务ID
     * @return 广播任务
     */
    public WorkCastTask selectWorkCastTaskById(Long taskId);

    /**
     * 查询广播任务列表
     * 
     * @param workCastTask 广播任务
     * @return 广播任务集合
     */
    public List<WorkCastTask> selectWorkCastTaskList(WorkCastTask workCastTask);

    /**
     * 新增广播任务
     * 
     * @param workCastTask 广播任务
     * @return 结果
     */
    public int insertWorkCastTask(WorkCastTask workCastTask);

    /**
     * 修改广播任务
     * 
     * @param workCastTask 广播任务
     * @return 结果
     */
    public int updateWorkCastTask(WorkCastTask workCastTask);

    /**
     * 删除广播任务
     * 
     * @param taskId 广播任务ID
     * @return 结果
     */
    public int deleteWorkCastTaskById(Long taskId);

    /**
     * 批量删除广播任务
     * 
     * @param taskIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteWorkCastTaskByIds(String[] taskIds);
    
    /**
     * 系统初始化删除全部缓存任务
     * 
     * @return 结果
     */
    public int deleteWorkCastTaskAll();
}
