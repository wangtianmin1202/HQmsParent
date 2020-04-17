package com.hand.spc.repository.dto;

import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.system.dto.BaseDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 判异规则
 */
@ApiModel("判异规则")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "pspc_judgement")
public class JudgementR extends BaseDTO {

    @Id
    @GeneratedValue
    @ApiModelProperty("附着对象ID")
    private Long judgementId;
    @ApiModelProperty("租户ID")
    private Long tenantId;
    @ApiModelProperty("站点ID")
    private Long siteId;
    @ApiModelProperty("准则代码简码")
    private String judgementShortCode; //准则代码简码
    @ApiModelProperty("准则代码")
    private String judgementCode; //准则代码
    @ApiModelProperty("$")
    private Long limitData; //$
    @ApiModelProperty("#")
    private Long lengthData; //#
    @ApiModelProperty("消息类型编码")
    private String messageTypeCode;
    @ApiModelProperty("判异规则组ID")
    private Long judgementGroupId;//判异规则组ID
    @Transient
    private String chartDetailType;//控制图明细类型（主图/次图）
    @Transient
    @ApiModelProperty("消息类型描述")
    private String messageTypeDesc;
    @Transient
    @ApiModelProperty("准则代码描述")
    private String judgementCodeDesc;
    @Transient
    @ApiModelProperty(hidden = true)
    private List<MessageTypeDetailR> messageTypeDetailList;//消息类型明细集合
    @Transient
    @ApiModelProperty(hidden = true)
    private List<MessageUploadConfigR> messageUploadConfigList;//消息命令集合

    public Long getJudgementId() {
        return judgementId;
    }

    public void setJudgementId(Long judgementId) {
        this.judgementId = judgementId;
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

    public String getJudgementShortCode() {
        return judgementShortCode;
    }

    public void setJudgementShortCode(String judgementShortCode) {
        this.judgementShortCode = judgementShortCode;
    }

    public String getJudgementCode() {
        return judgementCode;
    }

    public void setJudgementCode(String judgementCode) {
        this.judgementCode = judgementCode;
    }

    public Long getLimitData() {
        return limitData;
    }

    public void setLimitData(Long limitData) {
        this.limitData = limitData;
    }

    public Long getLengthData() {
        return lengthData;
    }

    public void setLengthData(Long lengthData) {
        this.lengthData = lengthData;
    }

    public Long getJudgementGroupId() {
        return judgementGroupId;
    }

    public void setJudgementGroupId(Long judgementGroupId) {
        this.judgementGroupId = judgementGroupId;
    }

    public String getChartDetailType() {
        return chartDetailType;
    }

    public void setChartDetailType(String chartDetailType) {
        this.chartDetailType = chartDetailType;
    }

    public String getMessageTypeCode() {
        return messageTypeCode;
    }

    public void setMessageTypeCode(String messageTypeCode) {
        this.messageTypeCode = messageTypeCode;
    }

    public String getJudgementCodeDesc() {
        return judgementCodeDesc;
    }

    public void setJudgementCodeDesc(String judgementCodeDesc) {
        this.judgementCodeDesc = judgementCodeDesc;
    }

    public String getMessageTypeDesc() {
        return messageTypeDesc;
    }

    public void setMessageTypeDesc(String messageTypeDesc) {
        this.messageTypeDesc = messageTypeDesc;
    }

    public List<MessageTypeDetailR> getMessageTypeDetailList() {
        return messageTypeDetailList;
    }

    public void setMessageTypeDetailList(List<MessageTypeDetailR> messageTypeDetailList) {
        this.messageTypeDetailList = messageTypeDetailList;
    }

    public List<MessageUploadConfigR> getMessageUploadConfigList() {
        return messageUploadConfigList;
    }

    public void setMessageUploadConfigList(List<MessageUploadConfigR> messageUploadConfigList) {
        this.messageUploadConfigList = messageUploadConfigList;
    }
}
