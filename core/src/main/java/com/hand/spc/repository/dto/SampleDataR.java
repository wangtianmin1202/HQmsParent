package com.hand.spc.repository.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.system.dto.BaseDTO;

import io.swagger.annotations.ApiModel;

@ApiModel("样本数据(计量)")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "pspc_sample_data")
public class SampleDataR extends  BaseDTO {

    @Id
    private Long sampleDataId;//样本数据ID 主键
    private Long tenantId;//租户ID
    private Long siteId;//站点ID
    private Long attachmentGroupId;//附着对象组ID
    private BigDecimal sampleValue;//样本值
    private Date sampleTime;//样本时间
    private Long ceGroupId;//控制要素组ID
    private Long ceParameterId;//控制要素ID
    private String edited;//是否编辑

    public Long getSampleDataId() {
        return sampleDataId;
    }

    public void setSampleDataId(Long sampleDataId) {
        this.sampleDataId = sampleDataId;
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

    public BigDecimal getSampleValue() {
        return sampleValue;
    }

    public void setSampleValue(BigDecimal sampleValue) {
        this.sampleValue = sampleValue;
    }

    public Date getSampleTime() {
        return sampleTime;
    }

    public void setSampleTime(Date sampleTime) {
        this.sampleTime = sampleTime;
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

    public String getEdited() {
        return edited;
    }

    public void setEdited(String edited) {
        this.edited = edited;
    }

}
