package com.hand.spc.repository.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.system.dto.BaseDTO;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AttachmentResponseDTO extends BaseDTO{   // 20190813 extends Child<AttachmentResponseDTO> implements SecurityToken

    @ApiModelProperty("附着对象主键")
    private Long attachmentId;
    @ApiModelProperty("租户ID")
    private Long tenantId; //租户ID
    @ApiModelProperty("站点ID")
    private Long siteId; //站点ID
    @ApiModelProperty("附着对象编码")
    private String attachmentCode; //附着对象
    @ApiModelProperty("附着对象类型")
    private String attachmentType; //附着对象类型
    @ApiModelProperty("附着对象描述")
    private String description; //描述
    @ApiModelProperty("父级附着对象ID")
    private Long parentAttachmentId; //父层级附着对象ID
    @ApiModelProperty("父级附着对象描述")
    private String parentDescription;//父层级附着对象描述
    @ApiModelProperty("父级附着对象code")
    private String parentCode;//父层级附着对象code

    @ApiModelProperty(hidden = true)
    private Date creationDate;
    @ApiModelProperty(hidden = true)
    private Long createdBy;
    @ApiModelProperty(hidden = true)
    private Date lastUpdateDate;
    @ApiModelProperty(hidden = true)
    private Long lastUpdatedBy;
    @ApiModelProperty("objectVersionNumber")
    private Long objectVersionNumber;
    @ApiModelProperty(hidden = true)
    private String _token;

    public Long getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(Long attachmentId) {
        this.attachmentId = attachmentId;
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

    public String getAttachmentCode() {
        return attachmentCode;
    }

    public void setAttachmentCode(String attachmentCode) {
        this.attachmentCode = attachmentCode;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getParentAttachmentId() {
        return parentAttachmentId;
    }

    public void setParentAttachmentId(Long parentAttachmentId) {
        this.parentAttachmentId = parentAttachmentId;
    }

    public String getParentDescription() {
        return parentDescription;
    }

    public void setParentDescription(String parentDescription) {
        this.parentDescription = parentDescription;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Long getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AttachmentResponse [attachmentId=");
        builder.append(attachmentId);
        builder.append(", tenantId=");
        builder.append(tenantId);
        builder.append(", siteId=");
        builder.append(siteId);
        builder.append(", attachmentCode=");
        builder.append(attachmentCode);
        builder.append(", attachmentType=");
        builder.append(attachmentType);
        builder.append(", tenantId=");
        builder.append(tenantId);
        builder.append(", description=");
        builder.append(description);
        builder.append(", description=");
        builder.append(description);
        builder.append(", parentAttachmentId=");
        builder.append(parentAttachmentId);
        builder.append(", parentDescription=");
        builder.append(parentDescription);
        builder.append(", creationDate=");
        builder.append(creationDate);
        builder.append(", createdBy=");
        builder.append(createdBy);
        builder.append(", lastUpdateDate=");
        builder.append(lastUpdateDate);
        builder.append(", lastUpdatedBy=");
        builder.append(lastUpdatedBy);
        builder.append(", objectVersionNumber=");
        builder.append(objectVersionNumber);
        builder.append("]");
        return builder.toString();
    }

    @Override
    public String get_token() {
        return _token;
    }
    @Override
    public void set_token(String _token) {
        this._token = _token;
    }
    /*@Override    20190813
    public Class<? extends SecurityToken> associateEntityClass() {
        return Attachment.class;
    }*/

}
