package com.hand.spc.repository.dto;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 消息类型
 */
@ExtensionAttribute(disable=true)
@Table(name = "pspc_message_type_detail")
public class MessageTypeDetailR extends BaseDTO {

    @Id
    @GeneratedValue
    private Long messageTypeDetailId;//消息类型明细ID
    private Long messageTypeId;//消息类型ID
    private Long tenantId;//租户ID
    private Long siteId;//站点ID
    private String elementCategory;//元素类别
    private String elementCode;//元素编码
    private String elementStatus;
    @Transient
    private String elementDescription;//元素描述

    public Long getMessageTypeDetailId() {
        return messageTypeDetailId;
    }

    public void setMessageTypeDetailId(Long messageTypeDetailId) {
        this.messageTypeDetailId = messageTypeDetailId;
    }

    public Long getMessageTypeId() {
        return messageTypeId;
    }

    public void setMessageTypeId(Long messageTypeId) {
        this.messageTypeId = messageTypeId;
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

    public String getElementStatus() {
        return elementStatus;
    }

    public void setElementStatus(String elementStatus) {
        this.elementStatus = elementStatus;
    }

    public String getElementDescription() {
        return elementDescription;
    }

    public void setElementDescription(String elementDescription) {
        this.elementDescription = elementDescription;
    }
}
