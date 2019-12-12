package com.audioweb.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.audioweb.common.annotation.DataScope;
import com.audioweb.common.constant.UserConstants;
import com.audioweb.common.core.domain.Ztree;
import com.audioweb.common.exception.BusinessException;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.system.domain.SysDomain;
import com.audioweb.system.domain.SysRole;
import com.audioweb.system.mapper.SysDomainMapper;
import com.audioweb.system.service.ISysDomainService;

/**
 * 分区管理 服务实现
 * 
 * @author shuofang
 */
@Service
public class SysDomainServiceImpl implements ISysDomainService
{
    @Autowired
    private SysDomainMapper domainMapper;

    /**
     * 查询分区管理数据
     * 
     * @param domain 分区信息
     * @return 分区信息集合
     */
    @Override
    @DataScope(domainAlias = "d")
    public List<SysDomain> selectDomainList(SysDomain domain)
    {
        return domainMapper.selectDomainList(domain);
    }

    /**
     * 查询分区管理树
     * 
     * @param domain 分区信息
     * @return 所有分区信息
     */
    @Override
    @DataScope(domainAlias = "d")
    public List<Ztree> selectDomainTree(SysDomain domain)
    {
        List<SysDomain> domainList = domainMapper.selectDomainList(domain);
        List<Ztree> ztrees = initZtree(domainList);
        return ztrees;
    }

    /**
     * 根据角色ID查询分区（数据权限）
     *
     * @param role 角色对象
     * @return 分区列表（数据权限）
     */
    @Override
    public List<Ztree> roleDomainTreeData(SysRole role)
    {
        Long roleId = role.getRoleId();
        List<Ztree> ztrees = new ArrayList<Ztree>();
        List<SysDomain> domainList = selectDomainList(new SysDomain());
        if (StringUtils.isNotNull(roleId))
        {
            List<String> roledomainList = domainMapper.selectRoleDomainTree(roleId);
            ztrees = initZtree(domainList, roledomainList);
        }
        else
        {
            ztrees = initZtree(domainList);
        }
        return ztrees;
    }

    /**
     * 对象转分区树
     *
     * @param domainList 分区列表
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<SysDomain> domainList)
    {
        return initZtree(domainList, null);
    }

    /**
     * 对象转分区树
     *
     * @param domainList 分区列表
     * @param roledomainList 角色已存在菜单列表
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<SysDomain> domainList, List<String> roledomainList)
    {

        List<Ztree> ztrees = new ArrayList<Ztree>();
        boolean isCheck = StringUtils.isNotNull(roledomainList);
        for (SysDomain domain : domainList)
        {
            if (UserConstants.DOMAIN_NORMAL.equals(domain.getStatus()))
            {
                Ztree ztree = new Ztree();
                ztree.setId(domain.getDomainId());
                ztree.setpId(domain.getParentId());
                ztree.setName(domain.getDomainName());
                ztree.setTitle(domain.getDomainName());
                if (isCheck)
                {
                    ztree.setChecked(roledomainList.contains(domain.getDomainId() + domain.getDomainName()));
                }
                ztrees.add(ztree);
            }
        }
        return ztrees;
    }

    /**
     * 查询分区人数
     * 
     * @param parentId 分区ID
     * @return 结果
     */
    @Override
    public int selectDomainCount(Long parentId)
    {
        SysDomain domain = new SysDomain();
        domain.setParentId(parentId);
        return domainMapper.selectDomainCount(domain);
    }

    /**
     * 查询分区是否存在用户
     * 
     * @param domainId 分区ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkDomainExistUser(Long domainId)
    {
        int result = domainMapper.checkDomainExistUser(domainId);
        return result > 0 ? true : false;
    }

    /**
     * 删除分区管理信息
     * 
     * @param domainId 分区ID
     * @return 结果
     */
    @Override
    public int deleteDomainById(Long domainId)
    {
        return domainMapper.deleteDomainById(domainId);
    }

    /**
     * 新增保存分区信息
     * 
     * @param domain 分区信息
     * @return 结果
     */
    @Override
    public int insertDomain(SysDomain domain)
    {
        SysDomain info = domainMapper.selectDomainById(domain.getParentId());
        // 如果父节点不为"正常"状态,则不允许新增子节点
        if (!UserConstants.DOMAIN_NORMAL.equals(info.getStatus()))
        {
            throw new BusinessException("分区停用，不允许新增");
        }
        domain.setAncestors(info.getAncestors() + "," + domain.getParentId());
        return domainMapper.insertDomain(domain);
    }

    /**
     * 修改保存分区信息
     * 
     * @param domain 分区信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateDomain(SysDomain domain)
    {
        SysDomain newParentdomain = domainMapper.selectDomainById(domain.getParentId());
        SysDomain olddomain = selectDomainById(domain.getDomainId());
        if (StringUtils.isNotNull(newParentdomain) && StringUtils.isNotNull(olddomain))
        {
            String newAncestors = newParentdomain.getAncestors() + "," + newParentdomain.getDomainId();
            String oldAncestors = olddomain.getAncestors();
            domain.setAncestors(newAncestors);
            updatedomainChildren(domain.getDomainId(), newAncestors, oldAncestors);
        }
        int result = domainMapper.updateDomain(domain);
        if (UserConstants.DOMAIN_NORMAL.equals(domain.getStatus()))
        {
            // 如果该分区是启用状态，则启用该分区的所有上级分区
            updateParentdomainStatus(domain);
        }
        return result;
    }

    /**
     * 修改该分区的父级分区状态
     * 
     * @param domain 当前分区
     */
    private void updateParentdomainStatus(SysDomain domain)
    {
        String updateBy = domain.getUpdateBy();
        domain = domainMapper.selectDomainById(domain.getDomainId());
        domain.setUpdateBy(updateBy);
        domainMapper.updateDomainStatus(domain);
    }

    /**
     * 修改子元素关系
     * 
     * @param domainId 被修改的分区ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updatedomainChildren(Long domainId, String newAncestors, String oldAncestors)
    {
        List<SysDomain> children = domainMapper.selectChildrenDomainById(domainId);
        for (SysDomain child : children)
        {
            child.setAncestors(child.getAncestors().replace(oldAncestors, newAncestors));
        }
        if (children.size() > 0)
        {
            domainMapper.updateDomainChildren(children);
        }
    }

    /**
     * 根据分区ID查询信息
     * 
     * @param domainId 分区ID
     * @return 分区信息
     */
    @Override
    public SysDomain selectDomainById(Long domainId)
    {
        return domainMapper.selectDomainById(domainId);
    }

    /**
     * 校验分区名称是否唯一
     * 
     * @param domain 分区信息
     * @return 结果
     */
    @Override
    public String checkDomainNameUnique(SysDomain domain)
    {
        Long domainId = StringUtils.isNull(domain.getDomainId()) ? -1L : domain.getDomainId();
        SysDomain info = domainMapper.checkDomainNameUnique(domain.getDomainName(), domain.getParentId());
        if (StringUtils.isNotNull(info) && info.getDomainId().longValue() != domainId.longValue())
        {
            return UserConstants.DOMAIN_NAME_NOT_UNIQUE;
        }
        return UserConstants.DOMAIN_NAME_UNIQUE;
    }
}
