package com.audioweb.work.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.audioweb.work.domain.WorkScheme;

/**
 * 读取任务分区服务
 * 
 * @author ruoyi
 */
@Service("scheme")
public class SchemeService
{
    @Autowired
    private IWorkSchemeService workSchemeService;
    
    /**
     * 获取全部定时任务分区
     * 
     * @param dictType 字典类型
     * @return 参数键值
     */
    public List<WorkScheme> getWorkScheme()
    {
        return workSchemeService.selectWorkSchemeList(new WorkScheme());
    }
    /**
     * 获取启用或停用任务分区
     * 
     * @param dictType 字典类型
     * @return 参数键值
     */
    public List<WorkScheme> getWorkScheme(String isuse)
    {
    	WorkScheme scheme = new WorkScheme();
    	scheme.setStatus(isuse);
    	return workSchemeService.selectWorkSchemeList(scheme);
    }
}
