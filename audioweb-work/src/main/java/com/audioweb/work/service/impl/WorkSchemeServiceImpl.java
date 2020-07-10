package com.audioweb.work.service.impl;

import java.util.List;

import com.audioweb.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.audioweb.work.mapper.WorkSchemeMapper;
import com.audioweb.work.domain.WorkScheme;
import com.audioweb.work.service.IWorkSchemeService;
import com.audioweb.common.core.text.Convert;

/**
 * 广播方案Service业务层处理
 * 
 * @author shuofang
 * @date 2020-04-28
 */
@Service
public class WorkSchemeServiceImpl implements IWorkSchemeService 
{
    @Autowired
    private WorkSchemeMapper workSchemeMapper;
    
    /**
     * 查询广播方案
     * 
     * @param schemeId 广播方案ID
     * @return 广播方案
     */
    @Override
    public WorkScheme selectWorkSchemeById(Long schemeId)
    {
        return workSchemeMapper.selectWorkSchemeById(schemeId);
    }

    /**
     * 查询广播方案列表
     * 
     * @param workScheme 广播方案
     * @return 广播方案
     */
    @Override
    public List<WorkScheme> selectWorkSchemeList(WorkScheme workScheme)
    {
        return workSchemeMapper.selectWorkSchemeList(workScheme);
    }

    /**
     * 新增广播方案
     * 
     * @param workScheme 广播方案
     * @return 结果
     */
    @Override
    public int insertWorkScheme(WorkScheme workScheme)
    {
        workScheme.setCreateTime(DateUtils.getNowDate());
        return workSchemeMapper.insertWorkScheme(workScheme);
    }

    /**
     * 修改广播方案
     * 
     * @param workScheme 广播方案
     * @return 结果
     */
    @Override
    public int updateWorkScheme(WorkScheme workScheme)
    {
        workScheme.setUpdateTime(DateUtils.getNowDate());
        return workSchemeMapper.updateWorkScheme(workScheme);
    }

    /**
     * 删除广播方案对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteWorkSchemeByIds(String ids)
    {
        return workSchemeMapper.deleteWorkSchemeByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除广播方案信息
     * 
     * @param schemeId 广播方案ID
     * @return 结果
     */
    @Override
    public int deleteWorkSchemeById(Long schemeId)
    {
        return workSchemeMapper.deleteWorkSchemeById(schemeId);
    }
}
