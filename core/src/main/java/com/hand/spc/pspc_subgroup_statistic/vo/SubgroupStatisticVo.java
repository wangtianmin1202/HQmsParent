package com.hand.spc.pspc_subgroup_statistic.vo;

import com.hand.spc.pspc_sample_subgroup_rel.dto.SampleSubgroupRel;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class SubgroupStatisticVo {

    private	Long	subgroupStatisticId		;//	表id，主键
    private	Long	tenantId		;//	租户id
    private	Long	siteId		;//	站点id
    private	Long	sampleSubgroupId		;//	分组id
    private	String	entityCode		;//	实体控制图编码
    private	String	entityVersion		;//	实体控制图版本
    private BigDecimal subgroupBar		;//	平均值
    private	BigDecimal	subgroupR		;//	极差
    private	BigDecimal	subgroupMax		;//	最大值
    private	BigDecimal	subgroupMin		;//	最小值
    private	BigDecimal	subgroupSigma		;//	标准差
    private	BigDecimal	subgroupMe		;//	中位数
    private	BigDecimal	subgroupRm		;//	移动极差
    private	Long	objectVersionNumber		;//	行版本号，用来处理锁

    private Date sampleSubgroupTime     ;//
    private List<SampleSubgroupRel> sampleSubgroupRelationList;//

    public Long getSubgroupStatisticId() {
        return subgroupStatisticId;
    }

    public void setSubgroupStatisticId(Long subgroupStatisticId) {
        this.subgroupStatisticId = subgroupStatisticId;
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

    public BigDecimal getSubgroupBar() {
        return subgroupBar;
    }

    public void setSubgroupBar(BigDecimal subgroupBar) {
        this.subgroupBar = subgroupBar;
    }

    public BigDecimal getSubgroupR() {
        return subgroupR;
    }

    public void setSubgroupR(BigDecimal subgroupR) {
        this.subgroupR = subgroupR;
    }

    public BigDecimal getSubgroupMax() {
        return subgroupMax;
    }

    public void setSubgroupMax(BigDecimal subgroupMax) {
        this.subgroupMax = subgroupMax;
    }

    public BigDecimal getSubgroupMin() {
        return subgroupMin;
    }

    public void setSubgroupMin(BigDecimal subgroupMin) {
        this.subgroupMin = subgroupMin;
    }

    public BigDecimal getSubgroupSigma() {
        return subgroupSigma;
    }

    public void setSubgroupSigma(BigDecimal subgroupSigma) {
        this.subgroupSigma = subgroupSigma;
    }

    public BigDecimal getSubgroupMe() {
        return subgroupMe;
    }

    public void setSubgroupMe(BigDecimal subgroupMe) {
        this.subgroupMe = subgroupMe;
    }

    public BigDecimal getSubgroupRm() {
        return subgroupRm;
    }

    public void setSubgroupRm(BigDecimal subgroupRm) {
        this.subgroupRm = subgroupRm;
    }

    public Long getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    public Date getSampleSubgroupTime() {
        return sampleSubgroupTime;
    }

    public void setSampleSubgroupTime(Date sampleSubgroupTime) {
        this.sampleSubgroupTime = sampleSubgroupTime;
    }

    public List<SampleSubgroupRel> getSampleSubgroupRelationList() {
        return sampleSubgroupRelationList;
    }

    public void setSampleSubgroupRelationList(List<SampleSubgroupRel> sampleSubgroupRelationList) {
        this.sampleSubgroupRelationList = sampleSubgroupRelationList;
    }
}
