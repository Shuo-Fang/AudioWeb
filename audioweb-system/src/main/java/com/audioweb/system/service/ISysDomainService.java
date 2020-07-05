package com.audioweb.system.service;

import java.util.List;
import com.audioweb.common.core.domain.Ztree;
import com.audioweb.system.domain.SysDomain;
import com.audioweb.system.domain.SysRole;

/**
 * 分区管理 服务层
 * 
 * @author shuofang
 */
public interface ISysDomainService
{
    /**
     * 查询分区管理数据
     * 
     * @param domain 分区信息
     * @return 分区信息集合
     */
    public List<SysDomain> selectDomainList(SysDomain domain);

    /**
     * 查询分区管理树
     * 
     * @param domain 分区信息
     * @return 所有分区信息
     */
    public List<Ztree> selectDomainTree(SysDomain domain);

    /**
     * 根据角色ID查询菜单
     *
     * @param role 角色对象
     * @return 菜单列表
     */
    public List<Ztree> roleDomainTreeData(SysRole role);

    /**
     * 查询分区人数
     * 
     * @param parentId 父分区ID
     * @return 结果
     */
    public int selectDomainCount(Long parentId);

    /**
     * 查询分区是否存在用户
     * 
     * @param domainId 分区ID
     * @return 结果 true 存在 false 不存在
     */
    public boolean checkDomainExistUser(Long domainId);

    /**
     * 删除分区管理信息
     * 
     * @param domainId 分区ID
     * @return 结果
     */
    public int deleteDomainById(Long domainId);

    /**
     * 新增保存分区信息
     * 
     * @param domain 分区信息
     * @return 结果
     */
    public int insertDomain(SysDomain domain);

    /**
     * 修改保存分区信息
     * 
     * @param domain 分区信息
     * @return 结果
     */
    public int updateDomain(SysDomain domain);

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
    public List<SysDomain> selectDomainListByIds(String precinct);
    
    /**
     * 根据分区ID批量查询子分区信息
     * 
     * @param domainId 分区ID
     * @return 分区信息
     */
    public List<SysDomain> selectChildrenDomainById(Long domainId);

    /**
     * 校验分区名称是否唯一
     * 
     * @param domain 分区信息
     * @return 结果
     */
    public String checkDomainNameUnique(SysDomain domain);

	/**
	 * @Title: checkDomainExistTerminal 
	 * @Description: 查询分区是否存在终端
	 * @param domainId
	 * @return boolean 返回类型 
	 * @throws Exception
	 * @author ShuoFang 
	 * @date 2020年3月3日 上午10:19:46
	 */
	boolean checkDomainExistTerminal(Long domainId);
}
