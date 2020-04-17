package com.hand.spc.repository.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.system.dto.BaseDTO;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CountOocResponseDTO extends BaseDTO {
    @ApiModelProperty("租户ID")
    private Long tenantId;
    @ApiModelProperty("站点ID")
    private Long siteId;
    @ApiModelProperty("Count OOC ID")
    private String countOocId;
    @ApiModelProperty("实体控制图编码")
    private String entityCode;
    @ApiModelProperty("实体控制图版本")
    private String entityVersion;
    @ApiModelProperty("控制要素组ID")
    private Long ceGroupId;
    @ApiModelProperty("控制要素组")
    private String ceGroup;
    @ApiModelProperty("控制要素ID")
    private Long ceParameterId;
    @ApiModelProperty("控制要素")
    private String ceParameter;
    @ApiModelProperty("控制图编码")
    private String chartCode;
    @ApiModelProperty("判异规则组")
    private String judgementGroupCode;
    @ApiModelProperty("判异规则")
    private String judgementCode;
    @ApiModelProperty("判异规则简称")
    private String judgementShortCode;
    @ApiModelProperty("判异规则描述")
    private String judgementDesc;//计算获取(LOV)
    @ApiModelProperty("消息类型编码")
    private String messageTypeCode;
    @ApiModelProperty("消息类型描述")
    private String messageTypeDesc;//计算获取(LOV)
    @ApiModelProperty("分类组ID")
    private Long classifyGroupId;
    @ApiModelProperty("分类组名称")
    private String classifyGroupName;
    @ApiModelProperty("分类项ID")
    private Long classifyId;
    @ApiModelProperty("分类项名称")
    private String classifyName;
    @ApiModelProperty("OOC状态编码")
    private String oocStatus;
    @ApiModelProperty("OOC状态描述")
    private String oocStatusDesc;//计算获取(LOV)
    @ApiModelProperty("附着对象描述")
    private String attachmentGroupDescription;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("创建时间")
    private Date creationDate;
    @ApiModelProperty("版本号")
    private Long objectVersionNumber;
    @ApiModelProperty("样本数据ID")
    private Long countSampleDataId;
    @ApiModelProperty("控制图明细类型")
    private String chartDetailType;
    @ApiModelProperty(hidden = true)
    private Long lengthData;//# LOV替换
    @ApiModelProperty(hidden = true)
    private Long limitData;//$ LOV替换

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

    public String getCountOocId() {
        return countOocId;
    }

    public void setCountOocId(String countOocId) {
        this.countOocId = countOocId;
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

    public Long getCeGroupId() {
        return ceGroupId;
    }

    public void setCeGroupId(Long ceGroupId) {
        this.ceGroupId = ceGroupId;
    }

    public String getCeGroup() {
        return ceGroup;
    }

    public void setCeGroup(String ceGroup) {
        this.ceGroup = ceGroup;
    }

    public Long getCeParameterId() {
        return ceParameterId;
    }

    public void setCeParameterId(Long ceParameterId) {
        this.ceParameterId = ceParameterId;
    }

    public String getCeParameter() {
        return ceParameter;
    }

    public void setCeParameter(String ceParameter) {
        this.ceParameter = ceParameter;
    }

    public String getChartCode() {
        return chartCode;
    }

    public void setChartCode(String chartCode) {
        this.chartCode = chartCode;
    }

    public String getJudgementGroupCode() {
        return judgementGroupCode;
    }

    public void setJudgementGroupCode(String judgementGroupCode) {
        this.judgementGroupCode = judgementGroupCode;
    }

    public String getJudgementCode() {
        return judgementCode;
    }

    public void setJudgementCode(String judgementCode) {
        this.judgementCode = judgementCode;
    }

    public String getJudgementDesc() {
        return judgementDesc;
    }

    public void setJudgementDesc(String judgementDesc) {
        this.judgementDesc = judgementDesc;
    }

    public String getJudgementShortCode() {
        return judgementShortCode;
    }

    public void setJudgementShortCode(String judgementShortCode) {
        this.judgementShortCode = judgementShortCode;
    }

    public String getMessageTypeCode() {
        return messageTypeCode;
    }

    public void setMessageTypeCode(String messageTypeCode) {
        this.messageTypeCode = messageTypeCode;
    }

    public String getMessageTypeDesc() {
        return messageTypeDesc;
    }

    public void setMessageTypeDesc(String messageTypeDesc) {
        this.messageTypeDesc = messageTypeDesc;
    }

    public Long getClassifyGroupId() {
        return classifyGroupId;
    }

    public void setClassifyGroupId(Long classifyGroupId) {
        this.classifyGroupId = classifyGroupId;
    }

    public String getClassifyGroupName() {
        return classifyGroupName;
    }

    public void setClassifyGroupName(String classifyGroupName) {
        this.classifyGroupName = classifyGroupName;
    }

    public Long getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(Long classifyId) {
        this.classifyId = classifyId;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    public String getOocStatus() {
        return oocStatus;
    }

    public void setOocStatus(String oocStatus) {
        this.oocStatus = oocStatus;
    }

    public String getOocStatusDesc() {
        return oocStatusDesc;
    }

    public void setOocStatusDesc(String oocStatusDesc) {
        this.oocStatusDesc = oocStatusDesc;
    }

    public String getAttachmentGroupDescription() {
        return attachmentGroupDescription;
    }

    public void setAttachmentGroupDescription(String attachmentGroupDescription) {
        this.attachmentGroupDescription = attachmentGroupDescription;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getChartDetailType() {
        return chartDetailType;
    }

    public void setChartDetailType(String chartDetailType) {
        this.chartDetailType = chartDetailType;
    }

    public Long getCountSampleDataId() {
        return countSampleDataId;
    }

    public void setCountSampleDataId(Long countSampleDataId) {
        this.countSampleDataId = countSampleDataId;
    }

    public Long getLengthData() {
        return lengthData;
    }

    public void setLengthData(Long lengthData) {
        this.lengthData = lengthData;
    }

    public Long getLimitData() {
        return limitData;
    }

    public void setLimitData(Long limitData) {
        this.limitData = limitData;
    }
}
