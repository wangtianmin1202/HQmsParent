package com.hand.spc.repository.dto;

import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hand.hap.system.dto.BaseDTO;

/**
 * 控制要素组
 */
//"附着对象层级关系")
@Table(name = "pspc_attachment")
public class AttachmentR extends BaseDTO {

    @Id
    @GeneratedValue
    private Long attachmentId;
    private Long tenantId; //租户ID
    private Long siteId; //站点ID
    private String attachmentCode; //附着对象
    private String attachmentType; //附着对象类型
    private String description; //描述
    private String level_o; //层级
    private Long parentAttachmentId; //父层级附着对象ID
    @Transient
    private List<Long> childList;//存放批量删除的attachmentId
    @Transient
    private Long attachmentRelationId;//附着对象与组关系表Id
    @Transient
    private String attachmentTypeMeaning;

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

    public String getLevel_o() {
        return level_o;
    }

    public void setLevel_o(String level_o) {
        this.level_o = level_o;
    }

    public Long getParentAttachmentId() {
        return parentAttachmentId;
    }

    public void setParentAttachmentId(Long parentAttachmentId) {
        this.parentAttachmentId = parentAttachmentId;
    }

    public List<Long> getChildList() {
        return childList;
    }

    public void setChildList(List<Long> childList) {
        this.childList = childList;
    }

    public Long getAttachmentRelationId() {
        return attachmentRelationId;
    }

    public void setAttachmentRelationId(Long attachmentRelationId) {
        this.attachmentRelationId = attachmentRelationId;
    }

    public String getAttachmentTypeMeaning() {
        return attachmentTypeMeaning;
    }

    public void setAttachmentTypeMeaning(String attachmentTypeMeaning) {
        this.attachmentTypeMeaning = attachmentTypeMeaning;
    }
}
