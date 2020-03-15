package com.audioweb.web.controller.file;

import java.io.File;
import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.audioweb.common.annotation.Log;
import com.audioweb.common.constant.WorkConstants;
import com.audioweb.common.enums.BusinessType;
import com.audioweb.work.domain.WorkFile;
import com.audioweb.work.service.IWorkFileService;
import com.audioweb.common.core.controller.BaseController;
import com.audioweb.common.core.domain.AjaxResult;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.common.utils.file.FileUtils;
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
    	workFile.setDelFlag(WorkConstants.AUDIOFILENORMAL);
    	if(StringUtils.isNotEmpty(workFile.getFilePath())) {
    		try {
    			File f = new File(workFile.getFilePath());
    			workFile.setFilePath((f.getPath()+"\\").replaceAll("\\\\","\\\\\\\\"));
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("文件类型配置地址有误，请检查");
			}
    	}
        startPage();
        List<WorkFile> list = workFileService.selectWorkFileList(workFile);
        return getDataTable(list);
    }

    /**
     * 导出音频任务中所有音频的存储序列信息列表
     */
    @RequiresPermissions("work:file:export")
    @Log(title = "音频存储信息", businessType = BusinessType.EXPORT)
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
/*    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    *//**
     * 新增保存音频任务中所有音频的存储序列信息
     *//*
    @RequiresPermissions("work:file:add")
    @Log(title = "音频任务中所有音频的存储序列信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(WorkFile workFile)
    {
        return toAjax(workFileService.insertWorkFile(workFile));
    }*/

    /**
     * 修改音频任务中所有音频的存储序列信息
     */
/*    @GetMapping("/edit/{fileId}")
    public String edit(@PathVariable("fileId") String fileId, ModelMap mmap)
    {
        WorkFile workFile = workFileService.selectWorkFileById(fileId);
        mmap.put("workFile", workFile);
        return prefix + "/edit";
    }*/

    /**
     * 修改保存音频任务中所有音频的存储序列信息
     */
/*    @RequiresPermissions("work:file:edit")
    @Log(title = "音频任务中所有音频的存储序列信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(WorkFile workFile)
    {
        return toAjax(workFileService.updateWorkFile(workFile));
    }*/

    /**
     * 删除音频任务中所有音频的存储序列信息
     */
    @RequiresPermissions("work:file:remove")
    @Log(title = "音频存储信息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(@RequestParam("ids")String ids,@RequestParam(value = "paths[]") String[] paths)
    {
    	for(String path :paths) {
    		FileUtils.deleteFile(path);
    	}
        return toAjax(workFileService.slowDeleteWorkFileByIds(ids));
    }
}
