package com.hand.spc.repository.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageTypeUploadRelDTO {

    @ApiModelProperty(value = "关系主键")
    private Long relId;

    @ApiModelProperty(value = "消息触发配置id")
    private Long uploadConfigId;

    @ApiModelProperty(value = "消息类型id")
    private Long messageTypeId;

    @ApiModelProperty(value = "配置编码")
    private String configCode;

    @ApiModelProperty("配置类型")
    private String configType;

    @ApiModelProperty("参数值")
    private String configValue;

    @ApiModelProperty("配置命令")
    private String configCommand;

    @ApiModelProperty("描述")
    private String descriptions;

    @ApiModelProperty("对象版本号")
    private Long objectVersionNumber;

    public Long getRelId() {
        return relId;
    }

    public void setRelId(Long relId) {
        this.relId = relId;
    }

    public Long getUploadConfigId() {
        return uploadConfigId;
    }

    public void setUploadConfigId(Long uploadConfigId) {
        this.uploadConfigId = uploadConfigId;
    }

    public Long getMessageTypeId() {
        return messageTypeId;
    }

    public void setMessageTypeId(Long messageTypeId) {
        this.messageTypeId = messageTypeId;
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

    public String getdescriptions() {
        return descriptions;
    }

    public void setdescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public Long getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    public String getConfigCommand() {
        return configCommand;
    }

    public void setConfigCommand(String configCommand) {
        this.configCommand = configCommand;
    }
}
