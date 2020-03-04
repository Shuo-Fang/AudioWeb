package com.audioweb.web.controller.terminal;

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
import com.audioweb.common.constant.UserConstants;
import com.audioweb.common.enums.BusinessType;
import com.audioweb.work.domain.WorkTerminal;
import com.audioweb.work.service.IWorkTerminalService;
import com.audioweb.common.core.controller.BaseController;
import com.audioweb.common.core.domain.AjaxResult;
import com.audioweb.common.utils.poi.ExcelUtil;
import com.audioweb.framework.util.ShiroUtils;
import com.audioweb.system.service.ISysDomainService;
import com.audioweb.common.core.page.TableDataInfo;

/**
 * 终端管理Controller
 * 
 * @author shuofang
 * @date 2020-03-01
 */
@Controller
@RequestMapping("/system/terminal")
public class WorkTerminalController extends BaseController
{
    private String prefix = "system/terminal";

    @Autowired
    private IWorkTerminalService workTerminalService;

    @Autowired
    private ISysDomainService domainService;
    
    @RequiresPermissions("system:terminal:view")
    @GetMapping()
    public String terminal()
    {
        return prefix + "/terminal";
    }

    /**
     * 查询终端管理列表
     */
    @RequiresPermissions("system:terminal:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(WorkTerminal workTerminal)
    {
        startPage();
        List<WorkTerminal> list = workTerminalService.selectWorkTerminalList(workTerminal);
        return getDataTable(list);
    }

    /**
     * 导出终端管理列表
     */
    @RequiresPermissions("system:terminal:export")
    @Log(title = "终端管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(WorkTerminal workTerminal)
    {
        List<WorkTerminal> list = workTerminalService.selectWorkTerminalList(workTerminal);
        ExcelUtil<WorkTerminal> util = new ExcelUtil<WorkTerminal>(WorkTerminal.class);
        return util.exportExcel(list, "terminal");
    }

    /**
     * 新增终端管理
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }
    /**
     * 新增分区
     */
    @GetMapping("/add/{parentId}")
    public String add(@PathVariable("parentId") Long parentId, ModelMap mmap)
    {
        mmap.put("domain", domainService.selectDomainById(parentId));
        return prefix + "/add";
    }
    /**
     * 新增保存终端管理
     */
    @RequiresPermissions("system:terminal:add")
    @Log(title = "终端管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(WorkTerminal workTerminal)
    {
    	if (UserConstants.USER_NAME_NOT_UNIQUE.equals(workTerminalService.checkIpUnique(workTerminal)))
        {
            return error("新增终端'" + workTerminal.getTerminalName() + "'失败，终端ID已存在");
        }
        else if (UserConstants.USER_PHONE_NOT_UNIQUE.equals(workTerminalService.checkIdUnique(workTerminal)))
        {
            return error("新增终端'" + workTerminal.getTerminalName() + "'失败，终端IP已存在");
        }
    	workTerminal.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(workTerminalService.insertWorkTerminal(workTerminal));
    }

    /**
     * 修改终端管理
     */
    @GetMapping("/edit/{terRealId}")
    public String edit(@PathVariable("terRealId") String terRealId, ModelMap mmap)
    {
        WorkTerminal workTerminal = workTerminalService.selectWorkTerminalById(terRealId);
        mmap.put("workTerminal", workTerminal);
        return prefix + "/edit";
    }

    /**
     * 修改保存终端管理
     */
    @RequiresPermissions("system:terminal:edit")
    @Log(title = "终端管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(WorkTerminal workTerminal)
    {
        return toAjax(workTerminalService.updateWorkTerminal(workTerminal));
    }

    /**
     * 删除终端管理
     */
    @RequiresPermissions("system:terminal:remove")
    @Log(title = "终端管理", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(workTerminalService.deleteWorkTerminalByIds(ids));
    }
    
    /**
     * 校验IP地址
     */
    @PostMapping("/checkIpUnique")
    @ResponseBody
    public String checkIpUnique(WorkTerminal workTerminal)
    {
        return workTerminalService.checkIpUnique(workTerminal);
    }
    /**
     * 校验终端ID
     */
    @PostMapping("/checkIdUnique")
    @ResponseBody
    public String checkIdUnique(WorkTerminal workTerminal)
    {
    	return workTerminalService.checkIdUnique(workTerminal);
    }
    /**
     * 终端状态修改
     */
    @Log(title = "终端管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:terminal:edit")
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(WorkTerminal workTerminal)
    {
        return toAjax(workTerminalService.changeStatus(workTerminal));
    }
}
