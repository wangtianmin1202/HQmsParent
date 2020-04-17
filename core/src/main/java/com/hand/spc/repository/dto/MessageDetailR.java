package com.hand.spc.repository.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.system.dto.BaseDTO;

//import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;

@ApiModel("消息明细")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "pspc_message_detail")
public class MessageDetailR extends BaseDTO {

    @Id
    @GeneratedValue
    private Long messageDetailId;//消息明细ID
    private String messageId;//消息ID
    private Long tenantId;//租户ID
    private Long siteId;//站点ID
    private String entityCode;//实体控制图编码
    private String entityVersion;//实体控制图版本
    private String elementCategory;//元素类别(主题、内容)
    private String elementCode;//元素编码
    private String elementDescription;//元素描述
    private String elementStatus;//元素状态
    private String elementValueCode;//元素值编码
    private String elementValueDescription;//元素值描述

    public Long getMessageDetailId() {
        return messageDetailId;
    }

    public void setMessageDetailId(Long messageDetailId) {
        this.messageDetailId = messageDetailId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
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

    public String getElementCategory() {
        return elementCategory;
    }

    public void setElementCategory(String elementCategory) {
        this.elementCategory = elementCategory;
    }

    public String getElementCode() {
        return elementCode;
    }

    public void setElementCode(String elementCode) {
        this.elementCode = elementCode;
    }

    public String getElementDescription() {
        return elementDescription;
    }

    public void setElementDescription(String elementDescription) {
        this.elementDescription = elementDescription;
    }

    public String getElementStatus() {
        return elementStatus;
    }

    public void setElementStatus(String elementStatus) {
        this.elementStatus = elementStatus;
    }

    public String getElementValueCode() {
        return elementValueCode;
    }

    public void setElementValueCode(String elementValueCode) {
        this.elementValueCode = elementValueCode;
    }

    public String getElementValueDescription() {
        return elementValueDescription;
    }

    public void setElementValueDescription(String elementValueDescription) {
        this.elementValueDescription = elementValueDescription;
    }
}
