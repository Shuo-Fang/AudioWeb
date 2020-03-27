package com.audioweb.web.controller.filecast;

import java.io.File;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.audioweb.work.domain.WorkFile;
import com.audioweb.work.service.IWorkFileService;
import com.audioweb.work.service.IWorkTerminalService;

import io.swagger.annotations.Api;

import com.audioweb.common.config.Global;
import com.audioweb.common.constant.WorkConstants;
import com.audioweb.common.core.controller.BaseController;
import com.audioweb.common.core.domain.Ztree;
import com.audioweb.common.core.page.TableDataInfo;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.common.utils.file.FileUtils;
import com.audioweb.system.service.ISysConfigService;
import com.audioweb.system.service.ISysDomainService;

/**
 * 文件广播页面相关Controller
 * 
 * @author shuofang
 * @date 2020-03-10
 */
@Api("文件广播管理")
@Controller
@RequestMapping("/work/filecast")
public class FileCastController extends BaseController
{
    private String prefix = "work/filecast";


    @Autowired
    private IWorkTerminalService workTerminalService;

    @Autowired
    private IWorkFileService workFileService;
    
    @Autowired
    private ISysDomainService domainService;
    
	@Autowired
    private ISysConfigService configService;
	
    @RequiresPermissions("work:filecast:view")
    @GetMapping()
    public String filecast()
    {
        return prefix + "/filecast";
    }
    
    /**
     * 多选择分区树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> selectDomainTrees(@RequestParam String domainIds,@RequestParam String terIds)
    {
        return workTerminalService.roleTerminalTreeData(domainIds, terIds);
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
    
}
