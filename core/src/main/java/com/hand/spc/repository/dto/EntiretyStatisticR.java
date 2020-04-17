package com.hand.spc.repository.dto;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.system.dto.BaseDTO;

import io.swagger.annotations.ApiModel;

@ApiModel("实体控制图整体指标")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "pspc_entirety_statistic")
public class EntiretyStatisticR extends BaseDTO {

    @Id
    @GeneratedValue
    private Long entiretyStatisticId;//实体控制图整体指标ID
    private Long tenantId;//租户ID
    private Long siteId;//站点ID
    private Long sampleSubgroupId;//分组ID
    private String entityCode;//实体控制图编码
    private String entityVersion;//实体控制图版本
    private String chartDetailType;//控制图明细类型
    private String controlLimitUsage;//控制限类型（NULL/FIXED/CALCULATION）
    private BigDecimal entiretyBar;//整体平均值
    private BigDecimal entiretySigma;//整体标准差
    private BigDecimal entiretyUsl;//整体规格上限
    private BigDecimal entiretyLsl;//整体规格下限
    private BigDecimal entiretyUcl;//整体控制上限
    private BigDecimal entiretyCl;//整体控制中心限
    private BigDecimal entiretyLcl;//整体控制下限
    private BigDecimal entiretyAd;//正态检验统计量
    private BigDecimal entiretyP;//正态检验P值
    private BigDecimal entiretyCp;//过程能力指数
    private BigDecimal entiretyCpk;//过程能力指数
    private BigDecimal entiretyPp;//过程性能指数
    private BigDecimal entiretyPpk;//过程性能指数

    public Long getEntiretyStatisticId() {
        return entiretyStatisticId;
    }

    public void setEntiretyStatisticId(Long entiretyStatisticId) {
        this.entiretyStatisticId = entiretyStatisticId;
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

    public String getChartDetailType() {
        return chartDetailType;
    }

    public void setChartDetailType(String chartDetailType) {
        this.chartDetailType = chartDetailType;
    }

    public String getControlLimitUsage() {
        return controlLimitUsage;
    }

    public void setControlLimitUsage(String controlLimitUsage) {
        this.controlLimitUsage = controlLimitUsage;
    }

    public BigDecimal getEntiretyBar() {
        return entiretyBar;
    }

    public void setEntiretyBar(BigDecimal entiretyBar) {
        DecimalFormat df = new DecimalFormat("0.0000");
        this.entiretyBar = new BigDecimal(df.format(entiretyBar));
    }

    public BigDecimal getEntiretySigma() {
        return entiretySigma;
    }

    public void setEntiretySigma(BigDecimal entiretySigma) {
        DecimalFormat df = new DecimalFormat("0.0000");
        this.entiretySigma = new BigDecimal(df.format(entiretySigma));
    }

    public BigDecimal getEntiretyUsl() {
        return entiretyUsl;
    }

    public void setEntiretyUsl(BigDecimal entiretyUsl) {
        this.entiretyUsl = entiretyUsl;
    }

    public BigDecimal getEntiretyLsl() {
        return entiretyLsl;
    }

    public void setEntiretyLsl(BigDecimal entiretyLsl) {
        this.entiretyLsl = entiretyLsl;
    }

    public BigDecimal getEntiretyUcl() {
        return entiretyUcl;
    }

    public void setEntiretyUcl(BigDecimal entiretyUcl) {
        DecimalFormat df = new DecimalFormat("0.0000");
        this.entiretyUcl = new BigDecimal(df.format(entiretyUcl));
    }

    public BigDecimal getEntiretyCl() {
        return entiretyCl;
    }

    public void setEntiretyCl(BigDecimal entiretyCl) {
        DecimalFormat df = new DecimalFormat("0.0000");
        this.entiretyCl = new BigDecimal(df.format(entiretyCl));
    }

    public BigDecimal getEntiretyLcl() {
        return entiretyLcl;
    }

    public void setEntiretyLcl(BigDecimal entiretyLcl) {
        DecimalFormat df = new DecimalFormat("0.0000");
        this.entiretyLcl = new BigDecimal(df.format(entiretyLcl));
    }

    public BigDecimal getEntiretyAd() {
        return entiretyAd;
    }

    public void setEntiretyAd(BigDecimal entiretyAd) {
        this.entiretyAd = entiretyAd;
    }

    public BigDecimal getEntiretyP() {
        return entiretyP;
    }

    public void setEntiretyP(BigDecimal entiretyP) {
        this.entiretyP = entiretyP;
    }

    public BigDecimal getEntiretyCp() {
        return entiretyCp;
    }

    public void setEntiretyCp(BigDecimal entiretyCp) {
        this.entiretyCp = entiretyCp;
    }

    public BigDecimal getEntiretyCpk() {
        return entiretyCpk;
    }

    public void setEntiretyCpk(BigDecimal entiretyCpk) {
        this.entiretyCpk = entiretyCpk;
    }

    public BigDecimal getEntiretyPp() {
        return entiretyPp;
    }

    public void setEntiretyPp(BigDecimal entiretyPp) {
        this.entiretyPp = entiretyPp;
    }

    public BigDecimal getEntiretyPpk() {
        return entiretyPpk;
    }

    public void setEntiretyPpk(BigDecimal entiretyPpk) {
        this.entiretyPpk = entiretyPpk;
    }
}

