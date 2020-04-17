package com.hand.spc.repository.dto;

import io.swagger.annotations.ApiModelProperty;

public class AttachmentLovDTO {

    @ApiModelProperty(value = "附着对象主键")
    private Long attachmentId;

    @ApiModelProperty(value = "附着对象编码")
    private String attachmentCode;

    @ApiModelProperty("附着对象描述")
    private String description;

    @ApiModelProperty("附着对象类型")
    //@LovValue(lovCode = "PSPC.ATTACHMENT.TYPE",meaningField = "attachmentTypeMeaning")
    private String attachmentType;

    @ApiModelProperty(hidden = true)
    private String attachmentTypeMeaning;

    @ApiModelProperty(value = "租户",required = true)
    private Long tenantId;

    @ApiModelProperty(value = "站点",required = true)
    private Long siteId;

    public Long getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(Long attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getAttachmentCode() {
        return attachmentCode;
    }

    public void setAttachmentCode(String attachmentCode) {
        this.attachmentCode = attachmentCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public String getAttachmentTypeMeaning() {
        return attachmentTypeMeaning;
    }

    public void setAttachmentTypeMeaning(String attachmentTypeMeaning) {
        this.attachmentTypeMeaning = attachmentTypeMeaning;
    }
}
