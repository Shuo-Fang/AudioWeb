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
import com.audioweb.common.enums.BusinessType;
import com.audioweb.work.domain.WorkTerminal;
import com.audioweb.work.service.IWorkTerminalService;
import com.audioweb.common.core.controller.BaseController;
import com.audioweb.common.core.domain.AjaxResult;
import com.audioweb.common.utils.poi.ExcelUtil;
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
     * 新增保存终端管理
     */
    @RequiresPermissions("system:terminal:add")
    @Log(title = "终端管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(WorkTerminal workTerminal)
    {
        return toAjax(workTerminalService.insertWorkTerminal(workTerminal));
    }

    /**
     * 修改终端管理
     */
    @GetMapping("/edit/{terminalId}")
    public String edit(@PathVariable("terminalId") String terminalId, ModelMap mmap)
    {
        WorkTerminal workTerminal = workTerminalService.selectWorkTerminalById(terminalId);
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
}
