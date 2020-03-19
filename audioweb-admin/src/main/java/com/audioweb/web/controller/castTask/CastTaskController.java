/**   
 * @Title: CastTaskController.java 
 * @Package com.audioweb.web.controller.castTask 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com
 * @date 2020年3月19日 上午11:32:47 
 * @version V1.0   
 */ 
package com.audioweb.web.controller.castTask;

import java.io.Serializable;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.audioweb.common.core.controller.BaseController;
import com.audioweb.common.core.domain.AjaxResult;
import com.audioweb.common.enums.CastWorkType;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.common.utils.bean.BeanUtils;
import com.audioweb.framework.util.ShiroUtils;
import com.audioweb.system.domain.SysUser;
import com.audioweb.work.domain.CastTask;
import com.audioweb.work.service.IWorkTerminalService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

/** 
 * @ClassName: CastTaskController 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author ShuoFang hengyu.zhu@chinacreator.com 1015510750@qq.com 
 * @date 2020年3月19日 上午11:32:47  
 */
@Controller
@RequestMapping("/work/cast")
public class CastTaskController extends BaseController{
	
	private String prefix = "work/cast";
	
	@Autowired
    private IWorkTerminalService workTerminalService;
	
	@RequiresPermissions("system:casttask:view")
    @GetMapping()
    public String terminal()
    {
        return prefix + "/task";
    }
	
	@ApiOperation("获取任务信息接口测试")
    @GetMapping("/getTask")
    @ResponseBody
    public AjaxResult getTask() {
    	AjaxResult result = success("获取成功");
    	CastTask task = new CastTask();
    	task.setTaskId(1244L);
    	task.setCastType(CastWorkType.FILE);
    	task.setIsCast(true);
    	task.setVol(34);
    	result.put(AjaxResult.DATA_TAG, task);
    	return result;
	}
	@ApiOperation("新增任务信息接口测试")
    @PostMapping("/addTask")
    @ApiImplicitParam(name = "task", value = "任务信息",  dataType = "CastWorkType" , paramType = "query")
    @ResponseBody
	public AjaxResult add(CastWorkType task) {
		if(StringUtils.isNotNull(task)) {
			System.out.println(task.toString());
		}
		return success();
	}
}
