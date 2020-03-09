package com.audioweb.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.audioweb.system.domain.SysDomain;

/**
 * 分区管理 数据层
 * 
 * @author shuofang
 */
public interface SysDomainMapper
{
    /**
     * 查询分区管理人数
     * 
     * @param domain 分区信息
     * @return 结果
     */
    public int selectDomainCount(SysDomain domain);

    /**
     * 查询分区是否存在用户
     * 
     * @param domainId 分区ID
     * @return 结果
     */
    public int checkDomainExistUser(Long domainId);
    
    /**
     * 查询分区是否存在终端
     * 
     * @param domainId 分区ID
     * @return 结果
     */
    public int checkDomainExistTerminal(Long domainId);

    /**
     * 查询分区管理数据
     * @param domain 分区信息
     * @return 分区信息集合
     */
    public List<SysDomain> selectDomainList(SysDomain domain);

    /**
     * 删除分区管理信息
     * 
     * @param domainId 分区ID
     * @return 结果
     */
    public int deleteDomainById(Long domainId);

    /**
     * 新增分区信息
     * 
     * @param domain 分区信息
     * @return 结果
     */
    public int insertDomain(SysDomain domain);

    /**
     * 修改分区信息
     * 
     * @param domain 分区信息
     * @return 结果
     */
    public int updateDomain(SysDomain domain);

    /**
     * 修改子元素关系
     * 
     * @param domains 子元素
     * @return 结果
     */
    public int updateDomainChildren(@Param("domains") List<SysDomain> domains);

    /**
     * 根据分区ID查询信息
     * 
     * @param domainId 分区ID
     * @return 分区信息
     */
    public SysDomain selectDomainById(Long domainId);
    
    /**
     * 根据分区ID批量查询信息
     * 
     * @param domainId 分区ID
     * @return 分区信息
     */
    public List<SysDomain> selectDomainListByIds(String[] precinct);

    /**
     * 校验分区名称是否唯一
     * 
     * @param domainName 分区名称
     * @param parentId 父分区ID
     * @return 结果
     */
    public SysDomain checkDomainNameUnique(@Param("domainName") String domainName, @Param("parentId") Long parentId);

    /**
     * 根据角色ID查询分区
     *
     * @param roleId 角色ID
     * @return 分区列表
     */
    public List<String> selectRoleDomainTree(Long roleId);

    /**
     * 修改所在分区的父级分区状态
     * 
     * @param domain 分区
     */
    public void updateDomainStatus(SysDomain domain);

    /**
     * 根据ID查询所有子分区
     * @param domainId 分区ID
     * @return 分区列表
     */
    public List<SysDomain> selectChildrenDomainById(Long domainId);
}
