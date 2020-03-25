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

import com.audioweb.common.annotation.DataScope;
import com.audioweb.common.constant.UserConstants;
import com.audioweb.work.domain.WorkTerminal;
import com.audioweb.work.service.IWorkTerminalService;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import com.audioweb.common.core.controller.BaseController;
import com.audioweb.common.core.domain.AjaxResult;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.framework.util.ShiroUtils;
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
    @ApiOperation("获取在线终端信息")
    @ApiImplicitParams ({
    	@ApiImplicitParam(name = "domainId", value = "需要获取的终端分区ID,默认就是获取的用户信息中的domainId,可以是这个分区下的下级分区", required = true, dataType = "String", paramType = "query"),
    	@ApiImplicitParam(name = "pageSize", value = "本次请求的记录数(就是一次请求返回多少数据，默认10个,可以给一个极大的数实现查询全部)", required = true, dataType = "String", paramType = "query"),
    	@ApiImplicitParam(name = "pageNum", value = "本次请求的分页数(就是第几次请求了默认从1开始)",   required = true, dataType = "String", paramType = "query"),
    	@ApiImplicitParam(name = "orderByColumn", value = "本次请求查询的数据是以什么进行排序的(默认terminalId,可以为WorkTerminal其他属性)", required = true, dataType = "String", paramType = "query"),
    	@ApiImplicitParam(name = "isAsc", value = "本次请求排序是升序(asc)还是降序(desc)", required = true, dataType = "String", paramType = "query"),
    })
    @GetMapping("/listAll")
    @RequiresPermissions("work:terminal:list")
    @ResponseBody
    @ApiResponses({
        @ApiResponse(code=500,message="传参出错"),
        @ApiResponse(code=0,message="获取成功")
    })
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public AjaxResult listAll(@RequestParam String domainId)
    {
    	try {
    		WorkTerminal workTerminal = new WorkTerminal();
    		if(StringUtils.isNotEmpty(domainId)) {
        		workTerminal.setDomainId(Long.parseLong(domainId));
        	}else {
        		workTerminal.setDomainId(ShiroUtils.getSysUser().getDomainId());
        	}
            startPage();
    		SysDomain domain = workTerminal.getDomain();
    		domain.setStatus(UserConstants.DOMAIN_NORMAL);
    		List<WorkTerminal> list = workTerminalService.selectWorkTerminalList(workTerminal);
    		WorkTerminal.loadAll(list);
    		AjaxResult result = success();
	    	result.put(AjaxResult.DATA_TAG, list);
	    	result.put("total", new PageInfo(list).getTotal());
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
