package com.hand.spc.repository.dto;

import java.math.BigDecimal;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.system.dto.BaseDTO;

import io.swagger.annotations.ApiModel;

@ApiModel("样本数据分组关系")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "pspc_sample_subgroup_relation")
public class SampleSubgroupRelationR extends BaseDTO {

    @Id
    @GeneratedValue
    private Long sampleSubgroupRelationId;//样本数据分组ID
    private Long tenantId;//租户ID
    private Long siteId;//站点ID
    private Long sampleSubgroupId;//样本数据分组ID
    private Long sampleDataId;//样本数据(计量)ID
    private BigDecimal sampleValue;//样本值
    private String entityCode;//实体控制图编码
    private String entityVersion;//实体控制图版本

    public Long getSampleSubgroupRelationId() {
        return sampleSubgroupRelationId;
    }

    public void setSampleSubgroupRelationId(Long sampleSubgroupRelationId) {
        this.sampleSubgroupRelationId = sampleSubgroupRelationId;
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

    public Long getSampleSubgroupId() {
        return sampleSubgroupId;
    }

    public void setSampleSubgroupId(Long sampleSubgroupId) {
        this.sampleSubgroupId = sampleSubgroupId;
    }

    public Long getSampleDataId() {
        return sampleDataId;
    }

    public void setSampleDataId(Long sampleDataId) {
        this.sampleDataId = sampleDataId;
    }

    public BigDecimal getSampleValue() {
        return sampleValue;
    }

    public void setSampleValue(BigDecimal sampleValue) {
        this.sampleValue = sampleValue;
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
}
