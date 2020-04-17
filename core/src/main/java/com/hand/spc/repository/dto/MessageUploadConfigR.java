package com.hand.spc.repository.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.system.dto.BaseDTO;

import org.hibernate.validator.constraints.NotBlank;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author xiao.tang02 2019-07-02 14:04:38
 */
@ApiModel("")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "pspc_message_upload_config")
public class MessageUploadConfigR extends BaseDTO {

    public static final String FIELD_UPLOAD_CONFIG_ID = "uploadConfigId";
    public static final String FIELD_CONFIG_CODE = "configCode";
    public static final String FIELD_CONFIG_TYPE = "configType";
    public static final String FIELD_CONFIG_VALUE = "configValue";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SITE_ID = "siteId";



    @ApiModelProperty("主键id")
    @Id
    @GeneratedValue
    private Long uploadConfigId;

    @ApiModelProperty(value = "配置编码", required = true)
    @NotBlank
    private String configCode;

    @ApiModelProperty(value = "配置类型", required = true)
    @NotBlank
    private String configType;

    @ApiModelProperty(value = "参数值", required = true)
    @NotBlank
    private String configValue;

    @ApiModelProperty(value = "配置命令")
    private String configCommand;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;

    @ApiModelProperty(value = "站点ID", required = true)
    @NotNull
    private Long siteId;

    //
    // getter/setter
    // ------------------------------------------------------------------------------
    public Long getUploadConfigId() {
        return uploadConfigId;
    }

    public void setUploadConfigId(Long uploadConfigId) {
        this.uploadConfigId = uploadConfigId;
    }


    public String getConfigCode() {
        return configCode;
    }

    public void setConfigCode(String configCode) {
        this.configCode = configCode;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getConfigCommand() {
        return configCommand;
    }

    public void setConfigCommand(String configCommand) {
        this.configCommand = configCommand;
    }
}
