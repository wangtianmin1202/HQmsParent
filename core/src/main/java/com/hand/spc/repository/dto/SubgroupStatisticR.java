package com.hand.spc.repository.dto;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.system.dto.BaseDTO;

import io.swagger.annotations.ApiModel;

@ApiModel("子组统计指标")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "pspc_subgroup_statistic")
public class SubgroupStatisticR extends BaseDTO {

    @Id
    @GeneratedValue
    private Long subgroupStatisticId;//子组统计指标ID
    private Long tenantId;//租户ID
    private Long siteId;//站点ID
    private Long sampleSubgroupId;//样本数据分组ID
    private String entityCode;//实体控制图编码
    private String entityVersion;//实体控制图版本
    private BigDecimal subgroupBar;//平均值
    private BigDecimal subgroupR;//极差
    private BigDecimal subgroupMax;//最大值
    private BigDecimal subgroupMin;//最小值
    private BigDecimal subgroupSigma;//标准差
    private BigDecimal subgroupMe;//中位数
    private BigDecimal subgroupRm;//移动极差

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
        DecimalFormat df = new DecimalFormat("0.0000");
        this.subgroupBar = new BigDecimal(df.format(subgroupBar));
    }

    public BigDecimal getSubgroupR() {
        return subgroupR;
    }

    public void setSubgroupR(BigDecimal subgroupR) {
        DecimalFormat df = new DecimalFormat("0.0000");
        this.subgroupR = new BigDecimal(df.format(subgroupR));
    }

    public BigDecimal getSubgroupMax() {
        return subgroupMax;
    }

    public void setSubgroupMax(BigDecimal subgroupMax) {
        DecimalFormat df = new DecimalFormat("0.0000");
        this.subgroupMax = new BigDecimal(df.format(subgroupMax));
    }

    public BigDecimal getSubgroupMin() {
        return subgroupMin;
    }

    public void setSubgroupMin(BigDecimal subgroupMin) {
        DecimalFormat df = new DecimalFormat("0.0000");
        this.subgroupMin = new BigDecimal(df.format(subgroupMin));
    }

    public BigDecimal getSubgroupSigma() {
        return subgroupSigma;
    }

    public void setSubgroupSigma(BigDecimal subgroupSigma) {
        DecimalFormat df = new DecimalFormat("0.0000");
        this.subgroupSigma = new BigDecimal(df.format(subgroupSigma));
    }

    public BigDecimal getSubgroupMe() {
        return subgroupMe;
    }

    public void setSubgroupMe(BigDecimal subgroupMe) {
        DecimalFormat df = new DecimalFormat("0.0000");
        this.subgroupMe = new BigDecimal(df.format(subgroupMe));
    }

    public BigDecimal getSubgroupRm() {
        return subgroupRm;
    }

    public void setSubgroupRm(BigDecimal subgroupRm) {
        if (!ObjectUtils.isEmpty(subgroupRm)) {
            DecimalFormat df = new DecimalFormat("0.0000");
            this.subgroupRm = new BigDecimal(df.format(subgroupRm));
        } else {
            this.subgroupRm = subgroupRm;
        }
    }

}
