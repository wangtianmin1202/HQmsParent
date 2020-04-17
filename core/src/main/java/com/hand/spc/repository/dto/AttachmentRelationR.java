package com.hand.spc.repository.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import io.swagger.annotations.ApiModelProperty;

/**
 * 控制要素组
 */
//附着对象组与附着对象关系")
@ExtensionAttribute(disable=true)
@Table(name = "pspc_attachment_relation")
public class AttachmentRelationR extends BaseDTO {

    @Id
    @GeneratedValue
    private Long attachmentRelationId;
    @ApiModelProperty("租户ID")
    private Long tenantId;
    @ApiModelProperty("站点ID")
    private Long siteId;
    @ApiModelProperty("附着对象组ID")
    private Long attachmentGroupId;
    @ApiModelProperty("附着对象ID")
    private Long attachmentId;

    public Long getAttachmentRelationId() {
        return attachmentRelationId;
    }

    public void setAttachmentRelationId(Long attachmentRelationId) {
        this.attachmentRelationId = attachmentRelationId;
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

    public Long getAttachmentGroupId() {
        return attachmentGroupId;
    }

    public void setAttachmentGroupId(Long attachmentGroupId) {
        this.attachmentGroupId = attachmentGroupId;
    }

    public Long getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(Long attachmentId) {
        this.attachmentId = attachmentId;
    }
}
