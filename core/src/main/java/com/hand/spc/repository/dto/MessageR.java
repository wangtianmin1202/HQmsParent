package com.hand.spc.repository.dto;

import java.util.List;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.system.dto.BaseDTO;

import io.swagger.annotations.ApiModel;

/**
 * 消息
 */
@ApiModel("消息")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "pspc_message")
public class MessageR extends BaseDTO {

    @Id
    private String messageId;//消息ID
    private Long tenantId;//租户ID
    private Long siteId;//站点ID
    private String oocId;//OOC ID
    private String entityCode;//实体控制图编码
    private String entityVersion;//实体控制图版本
    private String groupCode;//分组(实体控制图为单位)
    private String messageStatus;//消息状态

    @Transient
    private List<MessageDetailR> messageDetailList;

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

    public String getOocId() {
        return oocId;
    }

    public void setOocId(String oocId) {
        this.oocId = oocId;
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

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public List<MessageDetailR> getMessageDetailList() {
        return messageDetailList;
    }

    public void setMessageDetailList(List<MessageDetailR> messageDetailList) {
        this.messageDetailList = messageDetailList;
    }
    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }
}
