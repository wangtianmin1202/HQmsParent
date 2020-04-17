package com.hand.spc.repository.dto;

import java.util.Date;

import com.hand.hap.system.dto.BaseDTO;

/**
 *
 * @author slj
 * @date 2019-07-04
 */
public class MessageUploadDTO extends BaseDTO {

	private Long uploadConfigId;
	private String messageText;
	private String messageStatus;
	private String messageIds;
	private String configType;
	private String oocId;
	private String configValue;
	private Long tenantId; // 租户ID
	private Long siteId; // 站点ID
	private String groupCode;
	private String configCommand;
	private Date startTime;
	private Date endTime;

	public Long getUploadConfigId() {
		return uploadConfigId;
	}

	public void setUploadConfigId(Long uploadConfigId) {
		this.uploadConfigId = uploadConfigId;
	}

	public String getMessageStatus() {
		return messageStatus;
	}

	public void setMessageStatus(String messageStatus) {
		this.messageStatus = messageStatus;
	}

	public String getConfigType() {
		return configType;
	}

	public void setConfigType(String configType) {
		this.configType = configType;
	}

	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
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

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public String getMessageIds() {
		return messageIds;
	}

	public void setMessageIds(String messageIds) {
		this.messageIds = messageIds;
	}

	public String getConfigCommand() {
		return configCommand;
	}

	public void setConfigCommand(String configCommand) {
		this.configCommand = configCommand;
	}

	public Date getStartTime() {
		if (startTime != null) {
			return (Date) startTime.clone();
		} else {
			return null;
		}
	}

	public void setStartTime(Date startTime) {
		if (startTime == null) {
			this.startTime = null;
		} else {
			this.startTime = (Date) startTime.clone();
		}
	}

	public Date getEndTime() {
		if (endTime != null) {
			return (Date) endTime.clone();
		} else {
			return null;
		}
	}

	public void setEndTime(Date endTime) {
		if (endTime == null) {
			this.endTime = null;
		} else {
			this.endTime = (Date) endTime.clone();
		}
	}

	public String getOocId() {
		return oocId;
	}

	public void setOocId(String oocId) {
		this.oocId = oocId;
	}
}
