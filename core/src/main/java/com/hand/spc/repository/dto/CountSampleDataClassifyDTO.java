package com.hand.spc.repository.dto;

import com.hand.hap.system.dto.BaseDTO;

import io.swagger.annotations.ApiModelProperty;

public class CountSampleDataClassifyDTO extends BaseDTO {

    @ApiModelProperty("租户ID")
    private Long tenantId; //租户ID

    @ApiModelProperty("站点ID")
    private Long siteId; //站点ID

    @ApiModelProperty("样本数据主键")
    private Long countSampleDataId;

    @ApiModelProperty("控制要素主键")
    private Long ceParameterId;

    @ApiModelProperty("控制要素编码")
    private String ceParameter;

    @ApiModelProperty("控制要素名称")
    private String ceParameterName;

    @ApiModelProperty("分类组主键")
    private Long classifyGroupId;

    @ApiModelProperty("分类项主键")
    private Long classifyId;

    @ApiModelProperty("分类项编码")
    private String classifyCode;

    @ApiModelProperty("分类项描述")
    private String description;

    @ApiModelProperty("分类项值")
    private Long classifyCountValue;

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

    public Long getCountSampleDataId() {
        return countSampleDataId;
    }

    public void setCountSampleDataId(Long countSampleDataId) {
        this.countSampleDataId = countSampleDataId;
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

    public String getCeParameterName() {
        return ceParameterName;
    }

    public void setCeParameterName(String ceParameterName) {
        this.ceParameterName = ceParameterName;
    }

    public Long getClassifyGroupId() {
        return classifyGroupId;
    }

    public void setClassifyGroupId(Long classifyGroupId) {
        this.classifyGroupId = classifyGroupId;
    }

    public Long getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(Long classifyId) {
        this.classifyId = classifyId;
    }

    public String getClassifyCode() {
        return classifyCode;
    }

    public void setClassifyCode(String classifyCode) {
        this.classifyCode = classifyCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getClassifyCountValue() {
        return classifyCountValue;
    }

    public void setClassifyCountValue(Long classifyCountValue) {
        this.classifyCountValue = classifyCountValue;
    }
}
