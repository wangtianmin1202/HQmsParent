package com.hand.spc.pspc_attachment.dto;

/**Auto Generated By Hap Code Generator**/

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@ExtensionAttribute(disable=true)
@Table(name = "pspc_attachment")
public class SpcAttachment extends BaseDTO {

    public static final String FIELD_ATTACHMENT_ID = "attachmentId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_ATTACHMENT_CODE = "attachmentCode";
    public static final String FIELD_ATTACHMENT_TYPE = "attachmentType";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_LEVEL_O = "levelO";
    public static final String FIELD_PARENT_ATTACHMENT_ID = "parentAttachmentId";


    @Id
    @GeneratedValue
    private Long attachmentId; //表ID，主键

    private Long tenantId; //租户ID

    private Long siteId; //站点ID

    @Length(max = 50)
    private String attachmentCode; //附着对象

    @NotEmpty
    @Length(max = 50)
    private String attachmentType; //类型

    @Length(max = 150)
    private String description; //描述

    @Length(max = 50)
    private String levelO; //层级

    private Long parentAttachmentId; //父层级附着对象ID

    @Transient
    private Long attachmentGroupId; //附着对象组ID

    @Transient
    private Long attachmentRelationId; //附着对象组ID

    @Transient
    private Long ceGroupId;

    public Long getCeGroupId() {
        return ceGroupId;
    }

    public void setCeGroupId(Long ceGroupId) {
        this.ceGroupId = ceGroupId;
    }

    public void setAttachmentId(Long attachmentId){
        this.attachmentId = attachmentId;
    }

    public Long getAttachmentId(){
        return attachmentId;
    }

    public void setTenantId(Long tenantId){
        this.tenantId = tenantId;
    }

    public Long getTenantId(){
        return tenantId;
    }

    public void setSiteId(Long siteId){
        this.siteId = siteId;
    }

    public Long getSiteId(){
        return siteId;
    }

    public void setAttachmentCode(String attachmentCode){
        this.attachmentCode = attachmentCode;
    }

    public String getAttachmentCode(){
        return attachmentCode;
    }

    public void setAttachmentType(String attachmentType){
        this.attachmentType = attachmentType;
    }

    public String getAttachmentType(){
        return attachmentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLevelO() {
        return levelO;
    }

    public void setLevelO(String levelO) {
        this.levelO = levelO;
    }

    public void setParentAttachmentId(Long parentAttachmentId){
        this.parentAttachmentId = parentAttachmentId;
    }

    public Long getParentAttachmentId(){
        return parentAttachmentId;
    }

    public Long getAttachmentGroupId() {
        return attachmentGroupId;
    }

    public void setAttachmentGroupId(Long attachmentGroupId) {
        this.attachmentGroupId = attachmentGroupId;
    }

    public Long getAttachmentRelationId() {
        return attachmentRelationId;
    }

    public void setAttachmentRelationId(Long attachmentRelationId) {
        this.attachmentRelationId = attachmentRelationId;
    }
}
