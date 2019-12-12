package com.audioweb.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 角色和分区关联 sys_role_domain
 * 
 * @author ruoyi
 */
public class SysRoleDomain
{
    /** 角色ID */
    private Long roleId;
    
    /** 分区ID */
    private Long domainId;

    public Long getRoleId()
    {
        return roleId;
    }

    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }
    
    public Long getDomainId() {
		return domainId;
	}

	public void setDomainId(Long domainId) {
		this.domainId = domainId;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("roleId", getRoleId())
            .append("domainId", getDomainId())
            .toString();
    }
}
