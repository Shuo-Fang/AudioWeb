package com.audioweb.work.service;

import java.util.List;
import com.audioweb.work.domain.WorkScheme;

/**
 * 广播方案Service接口
 * 
 * @author shuofang
 * @date 2020-04-28
 */
public interface IWorkSchemeService 
{
    /**
     * 查询广播方案
     * 
     * @param schemeId 广播方案ID
     * @return 广播方案
     */
    public WorkScheme selectWorkSchemeById(Long schemeId);

    /**
     * 查询广播方案列表
     * 
     * @param workScheme 广播方案
     * @return 广播方案集合
     */
    public List<WorkScheme> selectWorkSchemeList(WorkScheme workScheme);

    /**
     * 新增广播方案
     * 
     * @param workScheme 广播方案
     * @return 结果
     */
    public int insertWorkScheme(WorkScheme workScheme);

    /**
     * 修改广播方案
     * 
     * @param workScheme 广播方案
     * @return 结果
     */
    public int updateWorkScheme(WorkScheme workScheme);

    /**
     * 批量删除广播方案
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteWorkSchemeByIds(String ids);

    /**
     * 删除广播方案信息
     * 
     * @param schemeId 广播方案ID
     * @return 结果
     */
    public int deleteWorkSchemeById(Long schemeId);
}
