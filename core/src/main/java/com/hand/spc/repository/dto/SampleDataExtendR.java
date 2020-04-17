package com.hand.spc.repository.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.system.dto.BaseDTO;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "pspc_sample_data_extend")
public class SampleDataExtendR extends BaseDTO {
    @Id
    @GeneratedValue
    private Long sampleDataExtendId;//样本数据扩展表ID
    private Long tenantId;//租户ID
    private Long siteId;//站点ID
    private Long sampleDataId;//样本数据ID
    private String extendAttribute;//扩展属性
    private String extendValue;//扩展值

    public Long getSampleDataExtendId() {
        return sampleDataExtendId;
    }

    public void setSampleDataExtendId(Long sampleDataExtendId) {
        this.sampleDataExtendId = sampleDataExtendId;
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

    public Long getSampleDataId() {
        return sampleDataId;
    }

    public void setSampleDataId(Long sampleDataId) {
        this.sampleDataId = sampleDataId;
    }

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
}
