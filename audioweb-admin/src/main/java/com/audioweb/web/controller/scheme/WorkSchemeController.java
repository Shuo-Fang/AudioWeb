package com.audioweb.web.controller.scheme;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.audioweb.common.annotation.Log;
import com.audioweb.common.enums.BusinessType;
import com.audioweb.work.domain.WorkScheme;
import com.audioweb.work.service.IWorkSchemeService;
import com.audioweb.common.core.controller.BaseController;
import com.audioweb.common.core.domain.AjaxResult;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.common.utils.poi.ExcelUtil;
import com.audioweb.framework.util.ShiroUtils;
import com.audioweb.common.core.page.TableDataInfo;

/**
 * 广播方案Controller
 * 
 * @author shuofang
 * @date 2020-04-28
 */
@Controller
@RequestMapping("/work/scheme")
public class WorkSchemeController extends BaseController
{
    private String prefix = "work/scheme";

    @Autowired
    private IWorkSchemeService workSchemeService;

    @RequiresPermissions("work:scheme:view")
    @GetMapping()
    public String scheme()
    {
        return prefix + "/scheme";
    }

    /**
     * 查询广播方案列表
     */
    @RequiresPermissions("work:scheme:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(WorkScheme workScheme)
    {
        startPage();
        List<WorkScheme> list = workSchemeService.selectWorkSchemeList(workScheme);
        return getDataTable(list);
    }

    /**
     * 导出广播方案列表
     */
    @RequiresPermissions("work:scheme:export")
    @Log(title = "广播方案", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(WorkScheme workScheme)
    {
        List<WorkScheme> list = workSchemeService.selectWorkSchemeList(workScheme);
        ExcelUtil<WorkScheme> util = new ExcelUtil<WorkScheme>(WorkScheme.class);
        return util.exportExcel(list, "scheme");
    }

    /**
     * 新增广播方案
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存广播方案
     */
    @RequiresPermissions("work:scheme:add")
    @Log(title = "广播方案", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(WorkScheme workScheme)
    {
    	if(StringUtils.isEmpty(workScheme.getSchemeName())) {
    		 return error("方案名称不能为空!");
    	}
    	workScheme.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(workSchemeService.insertWorkScheme(workScheme));
    }

    /**
     * 修改广播方案
     */
    @GetMapping("/edit/{schemeId}")
    public String edit(@PathVariable("schemeId") Long schemeId, ModelMap mmap)
    {
        WorkScheme workScheme = workSchemeService.selectWorkSchemeById(schemeId);
        mmap.put("workScheme", workScheme);
        return prefix + "/edit";
    }

    /**
     * 修改保存广播方案
     */
    @RequiresPermissions("work:scheme:edit")
    @Log(title = "广播方案", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(WorkScheme workScheme)
    {
        return toAjax(workSchemeService.updateWorkScheme(workScheme));
    }

    /**
     * 删除广播方案
     */
    @RequiresPermissions("work:scheme:remove")
    @Log(title = "广播方案", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(workSchemeService.deleteWorkSchemeByIds(ids));
    }
    /**
     * 终端状态修改
     */
    @Log(title = "广播方案", businessType = BusinessType.UPDATE)
    @RequiresPermissions("work:scheme:edit")
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(WorkScheme workScheme)
    {
        return toAjax(workSchemeService.updateWorkScheme(workScheme));
    }
    
}
