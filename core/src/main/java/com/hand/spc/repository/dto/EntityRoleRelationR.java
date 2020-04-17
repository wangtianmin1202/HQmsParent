package com.hand.spc.repository.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.system.dto.BaseDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 实体控制图与角色关系
 */
@ApiModel("实体控制图与角色关系")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "pspc_entity_role_relation")
public class EntityRoleRelationR extends BaseDTO {

    @Id
    @GeneratedValue
    private Long entityRoleRelationId;
    @ApiModelProperty("租户ID")
    private Long tenantId;
    @ApiModelProperty("站点ID")
    private Long siteId;
    @ApiModelProperty("实体控制图编码")
    private String entityCode;
    @ApiModelProperty("实体控制图版本")
    private String entityVersion;
    @ApiModelProperty("角色ID")
    private Long roleId;
    @Transient
    @ApiModelProperty("角色编码")
    private String roleCode;
    @Transient
    @ApiModelProperty("角色名称")
    private String roleName;
    @Transient
    @ApiModelProperty("角色描述")
    private String roleDescription;

    public Long getEntityRoleRelationId() {
        return entityRoleRelationId;
    }

    public void setEntityRoleRelationId(Long entityRoleRelationId) {
        this.entityRoleRelationId = entityRoleRelationId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getEntityCode() {
        return entityCode;
    }

    public void setEntityCode(String entityCode) {
        this.entityCode = entityCode;
    }

    public String getEntityVersion() {
        return entityVersion;
    }

    public void setEntityVersion(String entityVersion) {
        this.entityVersion = entityVersion;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }
}
