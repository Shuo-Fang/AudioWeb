package com.audioweb.web.controller.filecast;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.poi.xslf.usermodel.XSLFTableStyle.TablePartStyle;
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

import com.audioweb.work.domain.FileCastTask;
import com.audioweb.work.domain.WorkCastTask;
import com.audioweb.work.domain.WorkFile;
import com.audioweb.work.service.IWorkFileService;
import com.audioweb.work.service.IWorkTerminalService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import com.audioweb.common.annotation.Log;
import com.audioweb.common.config.Global;
import com.audioweb.common.constant.WorkConstants;
import com.audioweb.common.core.controller.BaseController;
import com.audioweb.common.core.domain.AjaxResult;
import com.audioweb.common.core.domain.Ztree;
import com.audioweb.common.core.page.TableDataInfo;
import com.audioweb.common.enums.BusinessType;
import com.audioweb.common.enums.CastWorkType;
import com.audioweb.common.enums.FileCastType;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.common.utils.file.FileUtils;
import com.audioweb.framework.util.ShiroUtils;
import com.audioweb.server.service.IWorkCastTaskService;
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

    @Autowired
    private IWorkCastTaskService workCastTaskService;
	
    @RequiresPermissions("work:filecast:view")
    @GetMapping()
    public String filecast(String taskId,ModelMap mmap)
    {
    	if(StringUtils.isNotEmpty(taskId)) {
    		mmap.put("taskId", taskId);
    	}
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
    @RequiresPermissions("work:filecast:view")
    @PostMapping("/findFileCast")
    @ResponseBody
    @ApiOperation("获取正在运行文件广播信息")
    @ApiImplicitParam(name = "taskId", value = "本次请求的广播任务ID，若为空，则为请求当前用户正在控制的广播任务信息，若返回data为空表示没有对应信息", dataType = "Long", paramType = "query")
    public AjaxResult findcast(Long taskId)
    {
    	FileCastTask task = null;
    	if(StringUtils.isNotNull(taskId)) {
    		WorkCastTask castTask = WorkCastTask.find(taskId);
    		if(castTask.getCastType() == CastWorkType.FILE && StringUtils.isNotNull(castTask)) {
    			try {
        			task = (FileCastTask) castTask;
				} catch (Exception e) {
					e.printStackTrace();
					task = null;
				}
    		}
    	}else {
        	String sessionId = ShiroUtils.getSessionId();
        	task = FileCastTask.findRunningTask(sessionId);
    	}
    	if(StringUtils.isNotNull(task)) {
    		AjaxResult result = success("获取成功");
	    	result.put(AjaxResult.DATA_TAG, task);
	    	return result;
    	}else {
    		return success("无任务信息");
    	}
    }
    
    @RequiresPermissions("work:filecast:add")
    @PostMapping("/startFileTask")
    @ResponseBody
    @ApiOperation("创建文件广播")
    @ApiImplicitParams ({
    	@ApiImplicitParam(name = "songData", value = "需要广播的音频id，逗号分隔，例如“1,2,3”,其中第一个为最开始播放的音频", required = true, dataType = "String", paramType = "query"),
    	@ApiImplicitParam(name = "fileCastType", value = "文件广播的模式:ORDER(顺序播放),LIST(列表循环),RANDOM(随机播放),SINGLE(单曲循环);例如传值：LIST 表示列表循环", required = true, dataType = "String", paramType = "query"),
    	@ApiImplicitParam(name = "vol", value = "本次文件广播初始化音量值,默认为30", required = true, dataType = "int", paramType = "query"),
    	@ApiImplicitParam(name = "castLevel", value = "本次文件广播的级别,值为0-5之间,0最高优先级,5最低,可以参考字典键值任务优先级", required = true, dataType = "int", paramType = "query"),
    	@ApiImplicitParam(name = "domainidlist", value = "本次文件广播的分区id列表,逗号分隔,例如“_100,101,102”，其中下划线表示当前分区并未全选，处于半选状态，就是说此分区下存在子分区或终端未选择", required = true, dataType = "String", paramType = "query"),
    	@ApiImplicitParam(name = "teridlist", value = "本次文件广播的终端realId列表,逗号分隔,可为空,例如“100000,100001,100002”，其中若此终端所处的分区为全选，则无需写入此列表,就是说这个列表里终端一定是处于半选的分区下的", dataType = "String", paramType = "query"),
    	@ApiImplicitParam(name = "taskName", value = "本次文件广播任务名称，默认可以为空", dataType = "String", paramType = "query"),
    })
    @Log(title = "文件广播任务", businessType = BusinessType.INSERT)
    public AjaxResult startFileTask(String songData,String fileCastType,Integer vol,Integer castLevel,String domainidlist,String teridlist,String taskName) {
    	FileCastTask task = new FileCastTask();
    	task.setSongData(songData);
    	task.setFileCastType(FileCastType.valueOf(fileCastType));
    	task.setVol(vol);
    	if(StringUtils.isNull(castLevel)) {
    		castLevel = 5;
    	}
    	task.setCastLevel(castLevel);
    	task.setDomainidlist(domainidlist);
    	task.setTeridlist(StringUtils.isNotEmpty(teridlist)?teridlist:"");
    	if(StringUtils.isNotEmpty(taskName)) {
    		task.setTaskName(taskName);
    	}else {
    		task.setTaskName(ShiroUtils.getSysUser().getUserName()+"_文件广播");
    	}
    	task.setCreateBy(ShiroUtils.getLoginName());
    	task.setCastType(CastWorkType.FILE);
    	task.setSessionId(ShiroUtils.getSessionId());
    	return workCastTaskService.insertWorkCastTask(task);
    }
}
