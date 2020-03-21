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
import com.audioweb.common.config.Global;
import com.audioweb.common.constant.WorkConstants;
import com.audioweb.common.enums.BusinessType;
import com.audioweb.work.domain.WorkFile;
import com.audioweb.work.service.IWorkFileService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import com.audioweb.common.core.controller.BaseController;
import com.audioweb.common.core.domain.AjaxResult;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.common.utils.file.FileUtils;
import com.audioweb.common.utils.poi.ExcelUtil;
import com.audioweb.system.service.ISysConfigService;
import com.audioweb.common.core.page.TableDataInfo;

/**
 * 音频任务中所有音频的存储序列信息Controller
 * 
 * @author shuofang
 * @date 2020-03-10
 */
@Api("音频文件信息管理")
@Controller
@RequestMapping("/work/file")
public class WorkFileController extends BaseController
{
    private String prefix = "work/file";

    @Autowired
    private IWorkFileService workFileService;
    
	@Autowired
    private ISysConfigService configService;
	
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
    	/**默认自动加载文件广播列表*/
		try {
			String filePath = Global.getFilePath();
			if(StringUtils.isNotEmpty(workFile.getFilePath())) {
				filePath = workFile.getFilePath();
			}
			File f = new File(filePath);
			workFile.setFilePath(FileUtils.formatToLin(FileUtils.formatPath(f.getPath()).concat("/")));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("文件类型配置地址有误，请检查");
		}
        startPage();
        List<WorkFile> list = workFileService.selectWorkFileList(workFile);
        return getDataTable(list);
    }
    
    /**
     * 查询音频任务中所有音频的存储序列信息列表
     */
    @ApiOperation("获取音频文件详细")
    @ApiImplicitParam(name = "type", value = "获取音频文件属性类型,work.file为文件广播文件,work.point为终端采播文件,work.word为文本广播文件", required = true, dataType = "String", paramType = "query")
    @PostMapping("/listAll")
    @ResponseBody
    @ApiResponses({
        @ApiResponse(code=500,message="传参出错"),
        @ApiResponse(code=0,message="获取成功")
    })
    public AjaxResult listAll(@RequestParam String type)
    {
    	try {
	    	WorkFile workFile = new WorkFile();
	    	workFile.setDelFlag(WorkConstants.AUDIOFILENORMAL);
	    	workFile.setFilePath(FileUtils.formatToLin(FileUtils.formatPath(configService.selectConfigByKey(type)).concat("/")));
	    	List<WorkFile> list = workFileService.selectWorkFileList(workFile);
	    	AjaxResult result = success("获取成功");
	    	result.put(AjaxResult.DATA_TAG, list);
	    	return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return error("传参出错");
    }

    /**
     * 导出音频任务中所有音频的存储序列信息列表
     */
    @RequiresPermissions("work:file:export")
    @Log(title = "音频信息", businessType = BusinessType.EXPORT)
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
    @Log(title = "音频信息", businessType = BusinessType.DELETE)
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
