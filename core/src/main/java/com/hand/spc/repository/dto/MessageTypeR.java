package com.hand.spc.repository.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;

//import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModelProperty;

/**
 * 消息类型
 */
@ExtensionAttribute(disable = true)
@Table(name = "pspc_message_type")
public class MessageTypeR /*extends AuditDomain*/ {

	@Id
	@GeneratedValue
	@ApiModelProperty("消息类型ID")
	private Long messageTypeId;
	@ApiModelProperty("租户ID")
	private Long tenantId;
	@ApiModelProperty("站点ID")
	private Long siteId;
	@ApiModelProperty("消息类型编码")
	private String messageTypeCode;
	@ApiModelProperty("消息类型状态(Y/N)")
	private String messageTypeStatus;
	@ApiModelProperty("消息类型明细(主题)")
	@Transient
	private List<MessageTypeDetailR> themeDetailList;
	@ApiModelProperty("消息类型明细(内容)")
	@Transient
	private List<MessageTypeDetailR> contentDetailList;
	
	private String description;
	
	private Long createdBy;
	private Date creationDate;
	private Long lastUpdatedBy;
	private Date lastUpdateDate;

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Long getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(Long lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
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

	public String getMessageTypeCode() {
		return messageTypeCode;
	}

	public void setMessageTypeCode(String messageTypeCode) {
		this.messageTypeCode = messageTypeCode;
	}

	public String getMessageTypeStatus() {
		return messageTypeStatus;
	}

	public void setMessageTypeStatus(String messageTypeStatus) {
		this.messageTypeStatus = messageTypeStatus;
	}

	public List<MessageTypeDetailR> getThemeDetailList() {
		return themeDetailList;
	}

	public void setThemeDetailList(List<MessageTypeDetailR> themeDetailList) {
		this.themeDetailList = themeDetailList;
	}

	public List<MessageTypeDetailR> getContentDetailList() {
		return contentDetailList;
	}

	public void setContentDetailList(List<MessageTypeDetailR> contentDetailList) {
		this.contentDetailList = contentDetailList;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
