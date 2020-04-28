package com.audioweb.work.mapper;

import java.util.List;
import com.audioweb.work.domain.WorkScheme;

/**
 * 广播方案Mapper接口
 * 
 * @author shuofang
 * @date 2020-04-28
 */
public interface WorkSchemeMapper 
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
     * 删除广播方案
     * 
     * @param schemeId 广播方案ID
     * @return 结果
     */
    public int deleteWorkSchemeById(Long schemeId);

    /**
     * 批量删除广播方案
     * 
     * @param schemeIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteWorkSchemeByIds(String[] schemeIds);
}
