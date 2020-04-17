package com.hand.spc.repository.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.hand.hap.system.dto.BaseDTO;

import io.swagger.annotations.ApiModelProperty;

public class SampleDataDTO extends  BaseDTO{

    @ApiModelProperty(value = "扩展属性")
    private String extendAttribute;
    @ApiModelProperty(value = "扩展值")
    private String extendValue;

    public String getExtendAttribute() {
        return extendAttribute;
    }

    public void setExtendAttribute(String extendAttribute) {
        this.extendAttribute = extendAttribute;
    }

    public String getExtendValue() {
        return extendValue;
    }

    public void setExtendValue(String extendValue) {
        this.extendValue = extendValue;
    }


    @ApiModelProperty(value = "预处理样本数据ID")
    private Long sampleDataWaitId;//暂时无法理解这个字段的作用



    @ApiModelProperty(value = "样本数据ID")
    private Long sampleDataId;//样本数据ID 主键
    @ApiModelProperty("租户ID")
    private Long tenantId; //租户ID
    @ApiModelProperty("站点ID")
    private Long siteId; //站点ID
    @ApiModelProperty("附着对象组ID")
    private Long attachmentGroupId;//附着对象组ID
    @ApiModelProperty("附着对象组描述")
    private String attachmentGroupDescription; //附着对象组描述
    @ApiModelProperty("控制要素组ID")
    private Long ceGroupId;//控制要素组ID
    @ApiModelProperty("控制要素组")
    private String ceGroup;//控制要素组
    @ApiModelProperty("控制要素ID")
    private Long ceParameterId;//控制要素ID
    @ApiModelProperty("控制要素")
    private String ceParameter;//控制要素
    @ApiModelProperty("样本时间")
    private String sampleTime; //样本时间
    @ApiModelProperty("样本开始时间")
    private Date sampleBeginTime;//样本开始时间
    @ApiModelProperty("样本结束时间")
    private Date sampleEndTime;//样本结束时间
    @ApiModelProperty("样本值")
    private BigDecimal sampleValue; //样本值
    @ApiModelProperty("扩展属性1")
    private String extendAttribute1; //扩展属性1
    @ApiModelProperty("扩展属性2")
    private String extendAttribute2; //扩展属性2
    @ApiModelProperty("扩展属性3")
    private String extendAttribute3; //扩展属性3
    @ApiModelProperty("扩展属性4")
    private String extendAttribute4; //扩展属性4
    @ApiModelProperty("扩展属性5")
    private String extendAttribute5; //扩展属性5
    @ApiModelProperty("模板编码")
    private String templateCode; //模板编码
    @ApiModelProperty("批次号")
    private String batch; //批次号
    @ApiModelProperty("样本扩展属性值，逗号拼接")
    private String sampleDataExtend;//样本扩展属性值，逗号拼接
    @ApiModelProperty("样本数据扩展属性")
    private List<SampleDataExtendR> sampleDataExtendList;
    @ApiModelProperty("录入时间")
    private Date creationDate;

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }


    public String getAttachmentGroupDescription() {
        return attachmentGroupDescription;
    }

    public void setAttachmentGroupDescription(String attachmentGroupDescription) {
        this.attachmentGroupDescription = attachmentGroupDescription;
    }

    public String getCeGroup() {
        return ceGroup;
    }

    public void setCeGroup(String ceGroup) {
        this.ceGroup = ceGroup;
    }

    public String getCeParameter() {
        return ceParameter;
    }

    public void setCeParameter(String ceParameter) {
        this.ceParameter = ceParameter;
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

    public Long getAttachmentGroupId() {
        return attachmentGroupId;
    }

    public void setAttachmentGroupId(Long attachmentGroupId) {
        this.attachmentGroupId = attachmentGroupId;
    }

    public Long getCeGroupId() {
        return ceGroupId;
    }

    public void setCeGroupId(Long ceGroupId) {
        this.ceGroupId = ceGroupId;
    }

    public Long getCeParameterId() {
        return ceParameterId;
    }

    public void setCeParameterId(Long ceParameterId) {
        this.ceParameterId = ceParameterId;
    }

    public String getSampleTime() {
        return sampleTime;
    }

    public void setSampleTime(String sampleTime) {
        this.sampleTime = sampleTime;
    }

    public BigDecimal getSampleValue() {
        return sampleValue;
    }

    public void setSampleValue(BigDecimal sampleValue) {
        this.sampleValue = sampleValue;
    }

    public String getExtendAttribute1() {
        return extendAttribute1;
    }

    public void setExtendAttribute1(String extendAttribute1) {
        this.extendAttribute1 = extendAttribute1;
    }

    public String getExtendAttribute2() {
        return extendAttribute2;
    }

    public void setExtendAttribute2(String extendAttribute2) {
        this.extendAttribute2 = extendAttribute2;
    }

    public String getExtendAttribute3() {
        return extendAttribute3;
    }

    public void setExtendAttribute3(String extendAttribute3) {
        this.extendAttribute3 = extendAttribute3;
    }

    public String getExtendAttribute4() {
        return extendAttribute4;
    }

    public void setExtendAttribute4(String extendAttribute4) {
        this.extendAttribute4 = extendAttribute4;
    }

    public String getExtendAttribute5() {
        return extendAttribute5;
    }

    public void setExtendAttribute5(String extendAttribute5) {
        this.extendAttribute5 = extendAttribute5;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public Date getSampleBeginTime() {
        return sampleBeginTime;
    }

    public void setSampleBeginTime(Date sampleBeginTime) {
        this.sampleBeginTime = sampleBeginTime;
    }

    public Date getSampleEndTime() {
        return sampleEndTime;
    }

    public void setSampleEndTime(Date sampleEndTime) {
        this.sampleEndTime = sampleEndTime;
    }

    public String getSampleDataExtend() {
        return sampleDataExtend;
    }

    public void setSampleDataExtend(String sampleDataExtend) {
        this.sampleDataExtend = sampleDataExtend;
    }

    public List<SampleDataExtendR> getSampleDataExtendList() {
        return sampleDataExtendList;
    }

    public void setSampleDataExtendList(List<SampleDataExtendR> sampleDataExtendList) {
        this.sampleDataExtendList = sampleDataExtendList;
    }
    public Long getSampleDataId() {
        return sampleDataId;
    }

    public void setSampleDataId(Long sampleDataId) {
        this.sampleDataId = sampleDataId;
    }

    public Long getSampleDataWaitId() {
        return sampleDataWaitId;
    }

    public void setSampleDataWaitId(Long sampleDataWaitId) {
        this.sampleDataWaitId = sampleDataWaitId;
    }

}
