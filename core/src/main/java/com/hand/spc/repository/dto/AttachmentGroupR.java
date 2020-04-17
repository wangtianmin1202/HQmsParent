package com.hand.spc.repository.dto;


import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

//附着对象组
@ExtensionAttribute(disable=true)
@Table(name = "pspc_attachment_group")
public class AttachmentGroupR extends BaseDTO {

    @Id
    @GeneratedValue
    private Long attachmentGroupId;//主键
    private Long tenantId;//租户Id
    private Long siteId;//
    private String attachmentGroupDescription;//附着对象组描述
    private Long ceGroupId;//控制要素组Id


    @Transient
    private Boolean entityUsed;

    @Transient
    private List<AttachmentRelationR> attachmentRelationList;// 附着对象组关系集合

    public Long getAttachmentGroupId() {
        return attachmentGroupId;
    }

    public void setAttachmentGroupId(Long attachmentGroupId) {
        this.attachmentGroupId = attachmentGroupId;
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

    public String getAttachmentGroupDescription() {
        return attachmentGroupDescription;
    }

    public void setAttachmentGroupDescription(String attachmentGroupDescription) {
        this.attachmentGroupDescription = attachmentGroupDescription;
    }

    public Long getCeGroupId() {
        return ceGroupId;
    }

    public void setCeGroupId(Long ceGroupId) {
        this.ceGroupId = ceGroupId;
    }

    public List<AttachmentRelationR> getAttachmentRelationList() {
        return attachmentRelationList;
    }

    public void setAttachmentRelationList(List<AttachmentRelationR> attachmentRelationList) {
        this.attachmentRelationList = attachmentRelationList;
    }

    public Boolean getEntityUsed() {
        return entityUsed;
    }

    public void setEntityUsed(Boolean entityUsed) {
        this.entityUsed = entityUsed;
    }
}
