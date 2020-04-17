package com.hand.spc.repository.dto;

import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.system.dto.BaseDTO;
import com.hand.spc.pspc_attachment_group.dto.AttachmentGroup;
import com.hand.spc.pspc_ce_relationship.dto.CeRelationship;

import io.swagger.annotations.ApiModel;

/**
 * 控制要素组
 */
@ApiModel("控制要素组")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "pspc_ce_group")
public class SPCCeGroup extends BaseDTO {

    @Id
    @GeneratedValue
    private Long ceGroupId;//主键

    @NotNull(message = "pspc.error.tenant.null")
    private Long tenantId;//租户

    @NotNull(message = "pspc.error.site.null")
    private Long siteId;//站点

    @NotBlank(message = "pspc.error.ce_group.null")
    private String ceGroup;//控制要素组

    private String description;//描述

    @NotBlank(message = "pspc.error.status_code.null")
    private String statusCode;//状态

    @Transient
    private List<CeParameterSPC> ceParameterList;//控制要素集合

    @Transient
    private List<CeRelationship> ceRelationshipList;//控制要素与组关系

    @Transient
    private List<AttachmentGroup> attachmentGroupList;//附着对象组集合

    public Long getCeGroupId() {
        return ceGroupId;
    }

    public void setCeGroupId(Long ceGroupId) {
        this.ceGroupId = ceGroupId;
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

    public String getCeGroup() {
        return ceGroup;
    }

    public void setCeGroup(String ceGroup) {
        this.ceGroup = ceGroup;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public List<CeParameterSPC> getCeParameterList() {
        return ceParameterList;
    }

    public void setCeParameterList(List<CeParameterSPC> ceParameterList) {
        this.ceParameterList = ceParameterList;
    }

    public List<AttachmentGroup> getAttachmentGroupList() {
        return attachmentGroupList;
    }

    public void setAttachmentGroupList(List<AttachmentGroup> attachmentGroupList) {
        this.attachmentGroupList = attachmentGroupList;
    }

    public List<CeRelationship> getCeRelationshipList() {
        return ceRelationshipList;
    }

    public void setCeRelationshipList(List<CeRelationship> ceRelationshipList) {
        this.ceRelationshipList = ceRelationshipList;
    }
}
