package com.audioweb.web.controller.terminal;

import java.util.ArrayList;
import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.audioweb.common.constant.UserConstants;
import com.audioweb.common.constant.WorkConstants;
import com.audioweb.work.domain.WorkTerminal;
import com.audioweb.work.service.IWorkTerminalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import com.audioweb.common.core.controller.BaseController;
import com.audioweb.common.core.domain.AjaxResult;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.system.domain.SysDomain;
import com.audioweb.system.service.ISysDomainService;
import com.audioweb.common.core.page.TableDataInfo;

/**
 * 终端管理Controller
 * 
 * @author shuofang
 * @date 2020-03-01
 */
@Controller
@RequestMapping("/work/terminal")
@Api("在线终端管理")
public class RunningTerminalController extends BaseController
{
    private String prefix = "work/terminal";

    @Autowired
    private IWorkTerminalService workTerminalService;

    @Autowired
    private ISysDomainService domainService;
    
    @RequiresPermissions("work:terminal:view")
    @GetMapping()
    public String terminal()
    {
        return prefix + "/terminal";
    }

    /**
     * 查询在线终端管理列表
     */
    @RequiresPermissions("work:terminal:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(WorkTerminal workTerminal)
    {
    	if(StringUtils.isNotNull(workTerminal.getDomainId())) {
    		SysDomain domain = workTerminal.getDomain();
    		domain.setStatus(UserConstants.DOMAIN_NORMAL);
    		workTerminal.setDomain(domain);
    	}
        startPage();
        List<WorkTerminal> list = workTerminalService.selectWorkTerminalList(workTerminal);
        WorkTerminal.loadAll(list);
        return getDataTable(list);
    }
    /**
     * 查询在线终端信息
     */
    @ApiOperation("根据根分区获取分区下所有在线终端信息")
    @ApiImplicitParam(name = "domainId", value = "需要获取的终端分区ID,默认就是获取的用户信息中的domainId,可以是这个分区下的下级分区", dataType = "String", paramType = "query")
    @GetMapping("/listAll")
    @RequiresPermissions("work:terminal:list")
    @ResponseBody
    @ApiResponses({
        @ApiResponse(code=500,message="传参出错"),
        @ApiResponse(code=0,message="获取成功")
    })
    public AjaxResult listAll(String domainId)
    {
    	try {
    		List<SysDomain> domains;
			SysDomain domain = new SysDomain();
			domain.setStatus(WorkConstants.NORMAL);
    		if(StringUtils.isNotEmpty(domainId)) {
    			domain.setDomainId(Long.parseLong(domainId));
    			domains = domainService.selectDomainList(domain);
        	}else {
    			domains = domainService.selectDomainList(domain);
        	}
    		String idString = "";
    		for(SysDomain sysDomain:domains) {
    			idString += sysDomain.getDomainId() + ",";
    		}
    		List<WorkTerminal> list = workTerminalService.selectWorkTerminalListByDomIds(idString);
    		WorkTerminal.loadAll(list);
    		AjaxResult result = success();
	    	result.put(AjaxResult.DATA_TAG, list);
	    	return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return error("传参出错");
    }
    /**
     * 查询在线终端信息
     */
    @ApiOperation("根据分区获取当前分区下在线终端信息")
    @ApiImplicitParam(name = "domainId", value = "需要获取的终端分区ID,只能获取当前分区下的终端，该分区下子分区终端无法获取", required = true, dataType = "String", paramType = "query")
    @GetMapping("/listByDomainId")
    @RequiresPermissions("work:terminal:list")
    @ResponseBody
    @ApiResponses({
    	@ApiResponse(code=500,message="传参出错"),
    	@ApiResponse(code=0,message="获取成功")
    })
    public AjaxResult listByDomainId(@RequestParam String domainId)
    {
    	try {
    		Long id = Long.parseLong(domainId);
    		List<WorkTerminal> list = workTerminalService.selectWorkTerminalListByDomId(id);
    		WorkTerminal.loadAll(list);
    		AjaxResult result = success();
    		result.put(AjaxResult.DATA_TAG, list);
    		return result;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return error("传参出错");
    }
    /**
     * 查询分区信息
     */
    @ApiOperation("获取当前分区下的下属分区")
    @GetMapping("/listDomain")
    @ResponseBody
    public AjaxResult listDomain()
    {
    	try {
    		List<SysDomain> list = domainService.selectDomainList(new SysDomain());
    		if(StringUtils.isNull(list)) {
    			list = new ArrayList<SysDomain>();
    		}
    		AjaxResult result = success();
    		result.put(AjaxResult.DATA_TAG, list);
    		return result;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return error("获取出错");
    }

}
