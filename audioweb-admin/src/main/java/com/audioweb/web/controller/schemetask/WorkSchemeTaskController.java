package com.audioweb.web.controller.schemetask;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.audioweb.common.annotation.Log;
import com.audioweb.common.core.controller.BaseController;
import com.audioweb.common.core.domain.AjaxResult;
import com.audioweb.common.core.page.TableDataInfo;
import com.audioweb.common.enums.BusinessType;
import com.audioweb.common.exception.job.TaskException;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.common.utils.poi.ExcelUtil;
import com.audioweb.quartz.domain.SysJob;
import com.audioweb.quartz.service.ISysJobService;
import com.audioweb.work.domain.WorkSchemeTask;
import com.audioweb.work.service.IWorkSchemeTaskService;

/**
 * 调度任务信息操作处理
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/work/schemeTask")
public class WorkSchemeTaskController extends BaseController
{
    private String prefix = "work/schemeTask";

    @Autowired
    private ISysJobService jobService;
    
    @Autowired
    private IWorkSchemeTaskService workSchemeTaskService;

    @RequiresPermissions("work:schemetask:view")
    @GetMapping()
    public String job(@RequestParam(value = "schemeId", required = false) Long schemeId, ModelMap mmap)
    {
    	if(StringUtils.isNotNull(schemeId)) {
    		mmap.put("schemeId", schemeId);
    	}
        return prefix + "/job";
    }

    @RequiresPermissions("work:schemetask:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(WorkSchemeTask task)
    {
        startPage();
        List<WorkSchemeTask> list = workSchemeTaskService.selectWorkSchemeTaskList(task);
        return getDataTable(list);
    }

    @Log(title = "定时任务", businessType = BusinessType.EXPORT)
    @RequiresPermissions("work:schemetask:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysJob job)
    {
        List<SysJob> list = jobService.selectJobList(job);
        ExcelUtil<SysJob> util = new ExcelUtil<SysJob>(SysJob.class);
        return util.exportExcel(list, "定时任务");
    }

    @Log(title = "定时任务", businessType = BusinessType.DELETE)
    @RequiresPermissions("work:schemetask:remove")
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) throws SchedulerException
    {
        jobService.deleteJobByIds(ids);
        return success();
    }

    @RequiresPermissions("work:schemetask:detail")
    @GetMapping("/detail/{jobId}")
    public String detail(@PathVariable("jobId") Long jobId, ModelMap mmap)
    {
        mmap.put("name", "job");
        mmap.put("job", jobService.selectJobById(jobId));
        return prefix + "/detail";
    }

    /**
     * 任务调度状态修改
     */
    @Log(title = "定时任务", businessType = BusinessType.UPDATE)
    @RequiresPermissions("work:schemetask:changeStatus")
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(SysJob job) throws SchedulerException
    {
        SysJob newJob = jobService.selectJobById(job.getJobId());
        newJob.setStatus(job.getStatus());
        return toAjax(jobService.changeStatus(newJob));
    }

    /**
     * 任务调度立即执行一次
     */
    @Log(title = "定时任务", businessType = BusinessType.RUN)
    @RequiresPermissions("work:schemetask:changeStatus")
    @PostMapping("/run")
    @ResponseBody
    public AjaxResult run(SysJob job) throws SchedulerException
    {
        jobService.run(job);
        return success();
    }

    /**
     * 新增调度
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存调度
     */
    @Log(title = "定时任务", businessType = BusinessType.INSERT)
    @RequiresPermissions("work:schemetask:add")
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysJob job) throws SchedulerException, TaskException
    {
        return toAjax(jobService.insertJob(job));
    }

    /**
     * 修改调度
     */
    @GetMapping("/edit/{jobId}")
    public String edit(@PathVariable("jobId") Long jobId, ModelMap mmap)
    {
        mmap.put("job", jobService.selectJobById(jobId));
        return prefix + "/edit";
    }

    /**
     * 修改保存调度
     */
    @Log(title = "定时任务", businessType = BusinessType.UPDATE)
    @RequiresPermissions("work:schemetask:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SysJob job) throws SchedulerException, TaskException
    {
        return toAjax(jobService.updateJob(job));
    }
    
    /**
     * 重置定时任务
     */
    @Log(title = "定时任务", businessType = BusinessType.UPDATE)
    @RequiresPermissions("work:schemetask:edit")
    @PostMapping("/reload")
    @ResponseBody
    public AjaxResult reload() throws SchedulerException, TaskException
    {
    	int size = jobService.init();
    	return toAjax(size > 0 ? size : 1);
    }

    /**
     * 校验cron表达式是否有效
     */
    @PostMapping("/checkCronExpressionIsValid")
    @ResponseBody
    public boolean checkCronExpressionIsValid(SysJob job)
    {
        return jobService.checkCronExpressionIsValid(job.getCronExpression());
    }
}
