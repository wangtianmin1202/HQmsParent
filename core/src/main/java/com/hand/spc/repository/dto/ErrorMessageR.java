package com.hand.spc.repository.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.core.annotation.MultiLanguageField;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import io.swagger.annotations.ApiModelProperty;

/**
 *
 *
 * @author linjie.shi@hand-china.com 2019-08-12 10:13:01
 */
@ExtensionAttribute(disable = true)
@Table(name = "pspc_error_message")
public class ErrorMessageR extends BaseDTO {

	public static final String FIELD_TENANT_ID = "tenantId";
	public static final String FIELD_MESSAGE_ID = "messageId";
	public static final String FIELD_MESSAGE_CODE = "messageCode";
	public static final String FIELD_MESSAGE = "message";
	public static final String FIELD_MODULE = "module";

	//
	// 业务方法(按public protected private顺序排列)
	// ------------------------------------------------------------------------------

	//
	// 数据库字段
	// ------------------------------------------------------------------------------


	@ApiModelProperty(value = "租户ID",required = true)
	@NotNull
	private Long tenantId;
	@ApiModelProperty("消息主键ID，唯一性标识")
	@Id
	@GeneratedValue
	private Long messageId;
	@ApiModelProperty(value = "消息编码",required = true)
	@NotBlank
	private String messageCode;
	@ApiModelProperty(value = "消息内容")
	@MultiLanguageField
	private String message;
	@ApiModelProperty(value = "服务包",required = true)
	@NotBlank
	private String module;


	//
	// 非数据库字段
	// ------------------------------------------------------------------------------

	//
	// getter/setter
	// ------------------------------------------------------------------------------

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
	 * @return 消息主键ID，唯一性标识
	 */
	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}
	/**
	 * @return 消息编码
	 */
	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}
	/**
	 * @return 消息内容
	 */
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return 服务包
	 */
	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	@Override
	public String toString() {
		return "ErrorMessage{" +
				"tenantId=" + tenantId +
				", messageId=" + messageId +
				", messageCode='" + messageCode + '\'' +
				", message='" + message + '\'' +
				", module='" + module + '\'' +
				'}';
	}
}
