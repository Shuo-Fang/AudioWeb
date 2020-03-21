package com.audioweb.web.controller.castTask;

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
import com.audioweb.work.domain.WorkCastTask;
import com.audioweb.work.service.IWorkCastTaskService;
import com.audioweb.common.core.controller.BaseController;
import com.audioweb.common.core.domain.AjaxResult;
import com.audioweb.common.utils.poi.ExcelUtil;
import com.audioweb.common.core.page.TableDataInfo;

/**
 * 广播任务Controller
 * 
 * @author shuofang
 * @date 2020-03-21
 */
@Controller
@RequestMapping("/work/castTask")
public class WorkCastTaskController extends BaseController
{
    private String prefix = "work/castTask";

    @Autowired
    private IWorkCastTaskService workCastTaskService;

    @RequiresPermissions("work:castTask:view")
    @GetMapping()
    public String castTask()
    {
        return prefix + "/castTask";
    }

    /**
     * 查询广播任务列表
     */
    @RequiresPermissions("work:castTask:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(WorkCastTask workCastTask)
    {
        startPage();
        List<WorkCastTask> list = workCastTaskService.selectWorkCastTaskList(workCastTask);
        return getDataTable(list);
    }

    /**
     * 导出广播任务列表
     */
    @RequiresPermissions("work:castTask:export")
    @Log(title = "广播任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(WorkCastTask workCastTask)
    {
        List<WorkCastTask> list = workCastTaskService.selectWorkCastTaskList(workCastTask);
        ExcelUtil<WorkCastTask> util = new ExcelUtil<WorkCastTask>(WorkCastTask.class);
        return util.exportExcel(list, "castTask");
    }

    /**
     * 新增广播任务
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存广播任务
     */
    @RequiresPermissions("work:castTask:add")
    @Log(title = "广播任务", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(WorkCastTask workCastTask)
    {
        return toAjax(workCastTaskService.insertWorkCastTask(workCastTask));
    }

    /**
     * 修改广播任务
     */
    @GetMapping("/edit/{taskId}")
    public String edit(@PathVariable("taskId") Long taskId, ModelMap mmap)
    {
        WorkCastTask workCastTask = workCastTaskService.selectWorkCastTaskById(taskId);
        mmap.put("workCastTask", workCastTask);
        return prefix + "/edit";
    }

    /**
     * 修改保存广播任务
     */
    @RequiresPermissions("work:castTask:edit")
    @Log(title = "广播任务", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(WorkCastTask workCastTask)
    {
        return toAjax(workCastTaskService.updateWorkCastTask(workCastTask));
    }

    /**
     * 删除广播任务
     */
    @RequiresPermissions("work:castTask:remove")
    @Log(title = "广播任务", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(workCastTaskService.deleteWorkCastTaskByIds(ids));
    }
}
