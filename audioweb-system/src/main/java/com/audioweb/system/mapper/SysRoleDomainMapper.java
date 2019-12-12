package com.audioweb.system.mapper;

import java.util.List;
import com.audioweb.system.domain.SysRoleDomain;

/**
 * 角色与分区关联表 数据层
 * 
 * @author ruoyi
 */
public interface SysRoleDomainMapper
{
    /**
     * 通过角色ID删除角色和分区关联
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    public int deleteRoleDomainByRoleId(Long roleId);

    /**
     * 批量删除角色分区关联信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRoleDomain(Long[] ids);

    /**
     * 查询分区使用数量
     * 
     * @param domainId 分区ID
     * @return 结果
     */
    public int selectCountRoleDomainByDomainId(Long domainId);

    /**
     * 批量新增角色分区信息
     * 
     * @param roleDomainList 角色分区列表
     * @return 结果
     */
    public int batchRoleDomain(List<SysRoleDomain> roleDomainList);
}
