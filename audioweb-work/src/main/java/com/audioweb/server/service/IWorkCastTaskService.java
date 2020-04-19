package com.audioweb.server.service;

import com.audioweb.common.core.domain.AjaxResult;
import com.audioweb.common.enums.FileCastCommand;
import com.audioweb.common.enums.FileCastType;
import com.audioweb.work.domain.FileCastTask;
import com.audioweb.work.domain.WorkCastTask;
import java.util.List;

/**
 * 广播任务Service接口
 * 
 * @author shuofang
 * @date 2020-03-21
 */
public interface IWorkCastTaskService 
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
    public List<WorkCastTask> selectWorkCastTaskList(WorkCastTask workCastTask,Integer pageNum,Integer pageSize);

    /**
     * 新增广播任务
     * 
     * @param workCastTask 广播任务
     * @return 结果
     */
    public AjaxResult insertWorkCastTask(WorkCastTask workCastTask);

    /**
     * 修改广播任务
     * 
     * @param workCastTask 广播任务
     * @return 结果
     */
    public int updateWorkCastTask(WorkCastTask workCastTask);

    /**
     * 批量删除广播任务
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteWorkCastTaskByIds(String ids);

    /**
     * 删除广播任务信息
     * 
     * @param taskId 广播任务ID
     * @return 结果
     */
    public int deleteWorkCastTaskById(Long taskId);
    /**
     * 控制广播任务,上下一曲,暂停启动
     * @Title: controlFileCastTask 
     * @Description: 控制广播任务 
     * @param taskId 任务ID
     * @param command 控制类型
     * @return AjaxResult 返回类型 
     * @throws 抛出错误
     * @author 10155 
     * @date 2020年4月19日 下午9:12:13
     */
	public AjaxResult controlFileCast(Long taskId, FileCastCommand command);
	/**
	 * 控制广播任务播放进度
	 * @Title: controlFileCastTask 
	 * @Description: 控制广播任务 
	 * @param taskId 任务ID
	 * @param playSite 控制进度
	 * @return AjaxResult 返回类型 
	 * @throws 抛出错误
	 * @author 10155 
	 * @date 2020年4月19日 下午9:12:13
	 */
	public AjaxResult controlFileCast(Long taskId, Long playSite);
	/**
	 * 控制广播任务模式
	 * @Title: controlFileCastTask 
	 * @Description: 控制广播任务 
	 * @param taskId 任务ID
	 * @param type 
	 * @return AjaxResult 返回类型 
	 * @throws 抛出错误
	 * @author 10155 
	 * @date 2020年4月19日 下午9:12:13
	 */
	public AjaxResult controlFileCast(Long taskId, FileCastType type);
	/**
	 * 控制广播任务音量
	 * @Title: controlFileCastTask 
	 * @Description: 控制广播任务 
	 * @param taskId 任务ID
	 * @param vol 音量
	 * @return AjaxResult 返回类型 
	 * @throws 抛出错误
	 * @author 10155 
	 * @date 2020年4月19日 下午9:12:13
	 */
	public AjaxResult controlFileCast(Long taskId, Integer vol);
}
