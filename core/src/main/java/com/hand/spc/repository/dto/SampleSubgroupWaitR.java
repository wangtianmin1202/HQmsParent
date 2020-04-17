package com.hand.spc.repository.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.system.dto.BaseDTO;

import io.swagger.annotations.ApiModel;

@ApiModel("样本待分组数据")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "pspc_sample_subgroup_wait")
public class SampleSubgroupWaitR extends BaseDTO {
    @Id
    @GeneratedValue
    private Long sampleSubgroupWaitId;//样本待分组ID
    private Long tenantId;//租户ID
    private Long siteId;//站点ID
    private Long sampleDataId;//样本数据ID
    private BigDecimal sampleValue;//样本值
    private Date sampleTime;//样本时间
    private String entityCode;//实体控制图编码
    private String entityVersion;//实体控制图版本
    private Long subgroupSize;//子组大小
    @Transient
    private List<Long> sampleSubgroupWaitIdList;//样本带分组ID集合

    public Long getSampleSubgroupWaitId() {
        return sampleSubgroupWaitId;
    }

    public void setSampleSubgroupWaitId(Long sampleSubgroupWaitId) {
        this.sampleSubgroupWaitId = sampleSubgroupWaitId;
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

    public Long getSubgroupSize() {
        return subgroupSize;
    }

    public void setSubgroupSize(Long subgroupSize) {
        this.subgroupSize = subgroupSize;
    }

    public List<Long> getSampleSubgroupWaitIdList() {
        return sampleSubgroupWaitIdList;
    }

    public void setSampleSubgroupWaitIdList(List<Long> sampleSubgroupWaitIdList) {
        this.sampleSubgroupWaitIdList = sampleSubgroupWaitIdList;
    }
}
