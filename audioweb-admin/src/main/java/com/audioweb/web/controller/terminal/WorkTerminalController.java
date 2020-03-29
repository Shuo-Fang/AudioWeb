package com.audioweb.web.controller.terminal;

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
import com.audioweb.common.annotation.Log;
import com.audioweb.common.constant.UserConstants;
import com.audioweb.common.constant.WorkConstants;
import com.audioweb.common.enums.BusinessType;
import com.audioweb.work.domain.WorkTerminal;
import com.audioweb.work.service.IWorkTerminalService;
import com.audioweb.common.core.controller.BaseController;
import com.audioweb.common.core.domain.AjaxResult;
import com.audioweb.common.utils.IpUtils;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.common.utils.poi.ExcelUtil;
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
@RequestMapping("/system/terminal")
public class WorkTerminalController extends BaseController
{
    private String prefix = "system/terminal";

    @Autowired
    private IWorkTerminalService workTerminalService;

    @Autowired
    private ISysDomainService domainService;
    
    @RequiresPermissions("system:terminal:view")
    @GetMapping()
    public String terminal()
    {
        return prefix + "/terminal";
    }

    /**
     * 查询终端管理列表
     */
    @RequiresPermissions("system:terminal:list")
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
        return getDataTable(list);
    }

    /**
     * 导出终端管理列表
     */
    @RequiresPermissions("system:terminal:export")
    @Log(title = "终端管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(WorkTerminal workTerminal)
    {
        List<WorkTerminal> list = workTerminalService.selectWorkTerminalList(workTerminal);
        ExcelUtil<WorkTerminal> util = new ExcelUtil<WorkTerminal>(WorkTerminal.class);
        return util.exportExcel(list, "terminal");
    }

    /**
     * 新增终端管理
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }
    /**
     * 新增终端
     */
    @GetMapping("/add/{parentId}")
    public String add(@PathVariable("parentId") Long parentId, ModelMap mmap)
    {
    	if(parentId != 0) {
            mmap.put("domain", domainService.selectDomainById(parentId));
    	}
        return prefix + "/add";
    }
    /**
     * 批量新增终端
     */
    @GetMapping("/addlist/{parentId}")
    public String addList(@PathVariable("parentId") Long parentId, ModelMap mmap)
    {
    	if(parentId != 0) {
    		mmap.put("domain", domainService.selectDomainById(parentId));
    	}
    	return prefix + "/addlist";
    }
    /**
     * 新增保存终端管理
     */
    @RequiresPermissions("system:terminal:add")
    @Log(title = "终端管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(WorkTerminal workTerminal)
    {
    	if (UserConstants.USER_NAME_NOT_UNIQUE.equals(workTerminalService.checkIpUnique(workTerminal)))
        {
            return error("新增终端'" + workTerminal.getTerminalName() + "'失败，终端ID已存在");
        }
        else if (UserConstants.USER_PHONE_NOT_UNIQUE.equals(workTerminalService.checkIdUnique(workTerminal)))
        {
            return error("新增终端'" + workTerminal.getTerminalName() + "'失败，终端IP已存在");
        }
    	workTerminal.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(workTerminalService.insertWorkTerminal(workTerminal));
    }
    /**
     * 批量新增保存终端管理
     */
    @RequiresPermissions("system:terminal:add")
    @Log(title = "终端管理", businessType = BusinessType.INSERT)
    @PostMapping("/addlist")
    @ResponseBody
    public AjaxResult addSaveList(WorkTerminal workTerminal)
    {
    	/** 特别注明，在批量新增终端中，其中采用了DelFlag作为判断是否覆盖已有终端,isOnline作为需要添加的终端数量值,terRealId作为终端名称递增值，domainName作为终端名后缀*/
    	String message = "";
    	String error = "";
    	int errorNum = 0;
    	try {
    		int terNum = workTerminal.getIsOnline();//需要添加的终端数量
    		int terNameNum = Integer.parseInt(workTerminal.getTerRealId());
    		String terPrefix = StringUtils.isEmpty(workTerminal.getTerminalName())?"":workTerminal.getTerminalName();
    		String terSuffix = StringUtils.isEmpty(workTerminal.getDomain().getDomainName())?"":workTerminal.getDomain().getDomainName();
    		int terId = Integer.parseInt(workTerminal.getTerminalId());
    		Long ip = IpUtils.ip2Long(workTerminal.getTerminalIp());
    		if(StringUtils.isNotEmpty(workTerminal.getDelFlag()) && workTerminal.getDelFlag().equals("0")) {
    			//为覆盖终端
    			//1.先删除原可能存在的终端
    			String delIds  = "";
    			for(int i = 0;i<terNum;i++) {
    				delIds += StringUtils.formatToTerId(terId+i)+",";
    			}
    			workTerminalService.deleteWorkTerminalByIds(delIds);
    			//2.批量新增终端
    			for(int i = 0;i<terNum;i++) {
    				WorkTerminal terminal = new WorkTerminal();
    				terminal.setTerminalIp(IpUtils.long2IP(ip+i));
    				if (WorkConstants.TERMINAL_IP_NOT_UNIQUE.equals(workTerminalService.checkIpUnique(terminal)))
    		    	{
        				terminal.setTerminalId(StringUtils.formatToTerId(terId+i));
        				terminal.setTerminalName(terPrefix+(terNameNum+i)+terSuffix);
    					error = error.concat("终端'").concat(terminal.getTerminalName()).concat("';IP:'").concat(terminal.getTerminalIp()).concat("'已存在，添加失败\r\n");
    					errorNum++;
    		    	}else {
        				terminal.setTerminalId(StringUtils.formatToTerId(terId+i));
        				terminal.setTerminalName(terPrefix+(terNameNum+i)+terSuffix);
    		    		terminal.setCreateBy(ShiroUtils.getLoginName());
    		    		terminal.setDomainId(workTerminal.getDomainId());
    		    		terminal.setIsAutoCast(workTerminal.getIsAutoCast());
    		    		terminal.setIsCmic(workTerminal.getIsCmic());
    		    		terminal.setRemark(workTerminal.getRemark());
    		    		terminal.setStatus(workTerminal.getStatus());
    		    		if(StringUtils.isNotEmpty(workTerminal.getPrecinct())) {
    		    			terminal.setPrecinct(workTerminal.getPrecinct());
    		    		}
    		    		try {
							if(workTerminalService.insertWorkTerminal(terminal) <= 0) {
								error = error.concat("终端'").concat(terminal.getTerminalName()).concat("'新增失败，请重试\r\n");
								errorNum++;
							}
						} catch (Exception e) {
							error = error.concat("终端'").concat(terminal.getTerminalName()).concat("'新增失败，请重试\r\n");
							errorNum++;
						}
    		    	}
    			}
    		}else {
    			//为跳过终端
    			//批量新增终端
    			for(int i = 0;i<terNum;i++) {
    				WorkTerminal terminal = new WorkTerminal();
    				terminal.setTerminalId(StringUtils.formatToTerId(terId+i));
    				if (WorkConstants.TERMINAL_ID_NOT_UNIQUE.equals(workTerminalService.checkIdUnique(terminal)))
    		    	{
        				terminal.setTerminalName(terPrefix+(terNameNum+i)+terSuffix);
    		    		error = error.concat("跳过新增终端'").concat(terminal.getTerminalName()).concat("'，终端ID已存在\r\n");
    		    		errorNum++;
    		    	}else {
    		    		terminal = new WorkTerminal();
        				terminal.setTerminalIp(IpUtils.long2IP(ip+i));
    		    		if (WorkConstants.TERMINAL_IP_NOT_UNIQUE.equals(workTerminalService.checkIpUnique(terminal)))
	    		    	{
	        				terminal.setTerminalName(terPrefix+(terNameNum+i)+terSuffix);
	    					error = error.concat("跳过新增终端'").concat(terminal.getTerminalName()).concat("';IP:'").concat(terminal.getTerminalIp()).concat("'该IP已存在终端\r\n");
	    					errorNum++;
	    		    	}else {
	        				terminal.setTerminalId(StringUtils.formatToTerId(terId+i));
	        				terminal.setTerminalName(terPrefix+(terNameNum+i)+terSuffix);
	    		    		terminal.setCreateBy(ShiroUtils.getLoginName());
	    		    		terminal.setDomainId(workTerminal.getDomainId());
	    		    		terminal.setIsAutoCast(workTerminal.getIsAutoCast());
	    		    		terminal.setIsCmic(workTerminal.getIsCmic());
	    		    		terminal.setRemark(workTerminal.getRemark());
	    		    		terminal.setStatus(workTerminal.getStatus());
	    		    		if(StringUtils.isNotEmpty(workTerminal.getPrecinct())) {
	    		    			terminal.setPrecinct(workTerminal.getPrecinct());
	    		    		}
	    		    		try {
								if(workTerminalService.insertWorkTerminal(terminal) <= 0) {
									error = error.concat("终端'").concat(terminal.getTerminalName()).concat("'新增失败，请重试\r\n");
									errorNum++;
								}
							} catch (Exception e) {
								error = error.concat("终端'").concat(terminal.getTerminalName()).concat("'新增失败，请重试\r\n");
								errorNum++;
							}
	    		    	}
    		    	}
    			}
        	}
    		message = "本次批量新增终端共'".concat(terNum+"").concat("'个，其中成功添加'").concat((terNum-errorNum)+"").concat("'个，失败或跳过'").concat(errorNum+"").concat("'个\r\n");
    		if(errorNum > 0) {
    			message = message.concat("详情如下：\r\n");
    		}
    		return success(message+error);
    	} catch (Exception e) {
			e.printStackTrace();
		}
		return error("批量新增失败，请检查填写是否正确或稍后再试");
    }
    
    /**
     * 修改终端管理
     */
    @GetMapping("/edit/{terRealId}")
    public String edit(@PathVariable("terRealId") String terRealId, ModelMap mmap)
    {
        WorkTerminal workTerminal = workTerminalService.selectWorkTerminalById(terRealId);
        if(StringUtils.isNotEmpty(workTerminal.getPrecinct())) {
        	String names = "";
        	List<SysDomain> list = domainService.selectDomainListByIds(workTerminal.getPrecinct());
        	for(SysDomain domain : list) {
        		names += domain.getDomainName()+",";
        	}
        	if(StringUtils.isNotEmpty(names)) {
        		workTerminal.setDelFlag(names.substring(0, names.length()-1));
        	}
        }else {
        	workTerminal.setDelFlag("");
        }
        mmap.put("workTerminal", workTerminal);
        return prefix + "/edit";
    }

    /**
     * 修改保存终端管理
     */
    @RequiresPermissions("system:terminal:edit")
    @Log(title = "终端管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(WorkTerminal workTerminal)
    {
        return toAjax(workTerminalService.updateWorkTerminal(workTerminal));
    }
    /**
     * 修改保存终端管理
     */
    @RequiresPermissions("system:terminal:edit")
    @Log(title = "终端管理", businessType = BusinessType.UPDATE)
    @PostMapping("/editdomain")
    @ResponseBody
    public AjaxResult editDomain(@RequestParam String ids,@RequestParam String domainId)
    {
    	return toAjax(workTerminalService.updateTerminalDomainByIds(domainId,ids));
    }

    /**
     * 删除终端管理
     */
    @RequiresPermissions("system:terminal:remove")
    @Log(title = "终端管理", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(workTerminalService.deleteWorkTerminalByIds(ids));
    }
    
    /**
     * 校验IP地址
     */
    @PostMapping("/checkIpUnique")
    @ResponseBody
    public String checkIpUnique(WorkTerminal workTerminal)
    {
        return workTerminalService.checkIpUnique(workTerminal);
    }
    /**
     * 校验终端ID
     */
    @PostMapping("/checkIdUnique")
    @ResponseBody
    public String checkIdUnique(WorkTerminal workTerminal)
    {
    	return workTerminalService.checkIdUnique(workTerminal);
    }
    /**
     * 终端状态修改
     */
    @Log(title = "终端管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:terminal:edit")
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(WorkTerminal workTerminal)
    {
        return toAjax(workTerminalService.changeStatus(workTerminal));
    }
    
    /**
     * 多选择分区树
     */
    @GetMapping("/selectDomainTrees/{domainId}")
    public String selectDomainTrees(@PathVariable("domainId") Long domainId, ModelMap mmap)
    {
        mmap.put("domain", domainService.selectDomainById(domainId));
        return prefix + "/checktree";
    }
}
