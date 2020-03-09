package com.audioweb.web.controller.system;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.audioweb.common.annotation.Log;
import com.audioweb.common.constant.UserConstants;
import com.audioweb.common.core.controller.BaseController;
import com.audioweb.common.core.domain.AjaxResult;
import com.audioweb.common.core.domain.Ztree;
import com.audioweb.common.enums.BusinessType;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.framework.util.ShiroUtils;
import com.audioweb.system.domain.SysDomain;
import com.audioweb.system.domain.SysRole;
import com.audioweb.system.service.ISysDomainService;

/**
 * 分区信息
 * 
 * @author shuofang
 */
@Controller
@RequestMapping("/system/domain")
public class SysDomainController extends BaseController
{
    private String prefix = "system/domain";

    @Autowired
    private ISysDomainService domainService;

    @RequiresPermissions("system:domain:view")
    @GetMapping()
    public String domain()
    {
        return prefix + "/domain";
    }

    @RequiresPermissions("system:domain:list")
    @PostMapping("/list")
    @ResponseBody
    public List<SysDomain> list(SysDomain domain)
    {
        List<SysDomain> domainList = domainService.selectDomainList(domain);
        return domainList;
    }

    /**
     * 新增分区
     */
    @GetMapping("/add/{parentId}")
    public String add(@PathVariable("parentId") Long parentId, ModelMap mmap)
    {
        mmap.put("domain", domainService.selectDomainById(parentId));
        return prefix + "/add";
    }

    /**
     * 新增保存分区
     */
    @Log(title = "分区管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("system:domain:add")
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysDomain domain)
    {
        if (UserConstants.DOMAIN_NAME_NOT_UNIQUE.equals(domainService.checkDomainNameUnique(domain)))
        {
            return error("新增分区'" + domain.getDomainName() + "'失败，分区名称已存在");
        }
        domain.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(domainService.insertDomain(domain));
    }

    /**
     * 修改
     */
    @GetMapping("/edit/{domainId}")
    public String edit(@PathVariable("domainId") Long domainId, ModelMap mmap)
    {
        SysDomain domain = domainService.selectDomainById(domainId);
        if (StringUtils.isNotNull(domain) && 100L == domainId)
        {
            domain.setParentName("无");
        }
        mmap.put("domain", domain);
        return prefix + "/edit";
    }

    /**
     * 保存
     */
    @Log(title = "分区管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:domain:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SysDomain domain)
    {
        if (UserConstants.DOMAIN_NAME_NOT_UNIQUE.equals(domainService.checkDomainNameUnique(domain)))
        {
            return error("修改分区'" + domain.getDomainName() + "'失败，分区名称已存在");
        }
        else if (domain.getParentId().equals(domain.getDomainId()))
        {
            return error("修改分区'" + domain.getDomainName() + "'失败，上级分区不能是自己");
        }
        domain.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(domainService.updateDomain(domain));
    }

    /**
     * 删除
     */
    @Log(title = "分区管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("system:domain:remove")
    @GetMapping("/remove/{domainId}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("domainId") Long domainId)
    {
        if (domainService.selectDomainCount(domainId) > 0)
        {
            return AjaxResult.warn("存在下级分区,不允许删除");
        }
        if (domainService.checkDomainExistUser(domainId))
        {
            return AjaxResult.warn("分区存在用户,不允许删除");
        }
        if (domainService.checkDomainExistTerminal(domainId))
        {
        	return AjaxResult.warn("分区存在终端,不允许删除");
        }
        return toAjax(domainService.deleteDomainById(domainId));
    }

    /**
     * 校验分区名称
     */
    @PostMapping("/checkDomainNameUnique")
    @ResponseBody
    public String checkDomainNameUnique(SysDomain domain)
    {
        return domainService.checkDomainNameUnique(domain);
    }

    /**
     * 选择分区树
     */
    @GetMapping("/selectDomainTree/{domainId}")
    public String selectDomainTree(@PathVariable("domainId") Long domainId, ModelMap mmap)
    {
        mmap.put("domain", domainService.selectDomainById(domainId));
        return prefix + "/tree";
    }

    /**
     * 多选择分区树
     */
    @GetMapping("/selectDomainTrees/{domainIds}")
    public String selectDomainTrees(@PathVariable("domainIds") String domainIds, ModelMap mmap)
    {
        mmap.put("treeIds", domainIds);
        return prefix + "/checktree";
    }

    /**
     * 加载分区列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData()
    {
        List<Ztree> ztrees = domainService.selectDomainTree(new SysDomain());
        return ztrees;
    }
    
    /**
     * 加载复选分区列表树
     */
    @GetMapping("/treesData/{domainIds}")
    @ResponseBody
    public List<Ztree> treesData(@PathVariable("domainIds") String domainIds)
    {
        List<Ztree> ztrees = domainService.selectDomainTree(new SysDomain());
        String[] ids = domainIds.split(",");
        for(String id:ids) {
        	for(Ztree tree:ztrees) {
        		if(tree.getId().equals(Long.parseLong(id))) {
        			tree.setChecked(true);
        			break;
        		}
        	}
        }
        return ztrees;
    }
    /**
     * 加载角色分区（数据权限）列表树
     */
    @GetMapping("/roleDomainTreeData")
    @ResponseBody
    public List<Ztree> domainTreeData(SysRole role)
    {
        List<Ztree> ztrees = domainService.roleDomainTreeData(role);
        return ztrees;
    }
}
