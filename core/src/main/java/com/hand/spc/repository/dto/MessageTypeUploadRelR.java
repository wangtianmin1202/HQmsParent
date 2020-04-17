package com.hand.spc.repository.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.system.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 
 *
 * @author xiao.tang02 2019-07-02 17:08:57
 */
@ApiModel("")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "pspc_message_type_upload_rel")
public class MessageTypeUploadRelR extends BaseDTO {

    public static final String FIELD_REL_ID = "relId";
    public static final String FIELD_UPLOAD_CONFIG_ID = "uploadConfigId";
    public static final String FIELD_MESSAGE_TYPE_ID = "messageTypeId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SITE_ID = "siteId";


    @ApiModelProperty("")
    @Id
    @GeneratedValue
    private Long relId;
    @ApiModelProperty(value = "",required = true)
    @NotNull
    private Long uploadConfigId;
    @ApiModelProperty(value = "",required = true)
    @NotNull
    private Long messageTypeId;
    @ApiModelProperty(value = "租户ID",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "站点ID",required = true)
    @NotNull
    private Long siteId;

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------

	@Transient
	private String messageTypeCode; //消息类型编码

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 
     */
	public Long getRelId() {
		return relId;
	}

	public void setRelId(Long relId) {
		this.relId = relId;
	}
    /**
     * @return 
     */
	public Long getUploadConfigId() {
		return uploadConfigId;
	}

	public void setUploadConfigId(Long uploadConfigId) {
		this.uploadConfigId = uploadConfigId;
	}
    /**
     * @return 
     */
	public Long getMessageTypeId() {
		return messageTypeId;
	}

	public void setMessageTypeId(Long messageTypeId) {
		this.messageTypeId = messageTypeId;
	}
    /**
     * @return 租户ID
     */
	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
    /**
     * @return 站点ID
     */
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
}
