package com.audioweb.web.controller.file;

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
import com.audioweb.work.domain.WorkFile;
import com.audioweb.work.service.IWorkFileService;
import com.audioweb.common.core.controller.BaseController;
import com.audioweb.common.core.domain.AjaxResult;
import com.audioweb.common.utils.poi.ExcelUtil;
import com.audioweb.common.core.page.TableDataInfo;

/**
 * 音频任务中所有音频的存储序列信息Controller
 * 
 * @author shuofang
 * @date 2020-03-10
 */
@Controller
@RequestMapping("/work/file")
public class WorkFileController extends BaseController
{
    private String prefix = "work/file";

    @Autowired
    private IWorkFileService workFileService;

    @RequiresPermissions("work:file:view")
    @GetMapping()
    public String file()
    {
        return prefix + "/file";
    }

    /**
     * 查询音频任务中所有音频的存储序列信息列表
     */
    @RequiresPermissions("work:file:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(WorkFile workFile)
    {
        startPage();
        List<WorkFile> list = workFileService.selectWorkFileList(workFile);
        return getDataTable(list);
    }

    /**
     * 导出音频任务中所有音频的存储序列信息列表
     */
    @RequiresPermissions("work:file:export")
    @Log(title = "音频任务中所有音频的存储序列信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(WorkFile workFile)
    {
        List<WorkFile> list = workFileService.selectWorkFileList(workFile);
        ExcelUtil<WorkFile> util = new ExcelUtil<WorkFile>(WorkFile.class);
        return util.exportExcel(list, "file");
    }

    /**
     * 新增音频任务中所有音频的存储序列信息
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存音频任务中所有音频的存储序列信息
     */
    @RequiresPermissions("work:file:add")
    @Log(title = "音频任务中所有音频的存储序列信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(WorkFile workFile)
    {
        return toAjax(workFileService.insertWorkFile(workFile));
    }

    /**
     * 修改音频任务中所有音频的存储序列信息
     */
    @GetMapping("/edit/{fileId}")
    public String edit(@PathVariable("fileId") String fileId, ModelMap mmap)
    {
        WorkFile workFile = workFileService.selectWorkFileById(fileId);
        mmap.put("workFile", workFile);
        return prefix + "/edit";
    }

    /**
     * 修改保存音频任务中所有音频的存储序列信息
     */
    @RequiresPermissions("work:file:edit")
    @Log(title = "音频任务中所有音频的存储序列信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(WorkFile workFile)
    {
        return toAjax(workFileService.updateWorkFile(workFile));
    }

    /**
     * 删除音频任务中所有音频的存储序列信息
     */
    @RequiresPermissions("work:file:remove")
    @Log(title = "音频任务中所有音频的存储序列信息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(workFileService.deleteWorkFileByIds(ids));
    }
}
