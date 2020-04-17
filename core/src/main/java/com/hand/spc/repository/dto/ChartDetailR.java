package com.hand.spc.repository.dto;

import java.math.BigDecimal;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.system.dto.BaseDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("控制图明细")
@Table(name = "pspc_chart_detail")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChartDetailR extends BaseDTO {

	@Id
    @GeneratedValue
    private Long chartDetailId;
    private Long tenantId;
    private Long siteId;
    private Long chartId;
    private String chartDetailType;
    private String xAxisLabel;
    private String yAxisLabel;
    private Long yAxisMax;
    private Long yAxisMin;
    private String controlLimitUsage;
    private BigDecimal upperControlLimit;
    private BigDecimal centerLine;
    private BigDecimal lowerControlLimit;
    private String displaySpecLimit;
    private BigDecimal upperSpecLimit;
    private BigDecimal specTarget;
    private BigDecimal lowerSpecLimit;
    private String enableJudgeGroup;
    private Long judgementGroupId;
    @Transient
    @ApiModelProperty("判异规则组编码")
    private String judgementGroupCode;

    public Long getChartDetailId() {
        return chartDetailId;
    }

    public void setChartDetailId(Long chartDetailId) {
        this.chartDetailId = chartDetailId;
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

    public Long getChartId() {
        return chartId;
    }

    public void setChartId(Long chartId) {
        this.chartId = chartId;
    }

    public String getChartDetailType() {
        return chartDetailType;
    }

    public void setChartDetailType(String chartDetailType) {
        this.chartDetailType = chartDetailType;
    }

    public String getxAxisLabel() {
        return xAxisLabel;
    }

    public void setxAxisLabel(String xAxisLabel) {
        this.xAxisLabel = xAxisLabel;
    }

    public String getyAxisLabel() {
        return yAxisLabel;
    }

    public void setyAxisLabel(String yAxisLabel) {
        this.yAxisLabel = yAxisLabel;
    }

    public Long getyAxisMax() {
        return yAxisMax;
    }

    public void setyAxisMax(Long yAxisMax) {
        this.yAxisMax = yAxisMax;
    }

    public Long getyAxisMin() {
        return yAxisMin;
    }

    public void setyAxisMin(Long yAxisMin) {
        this.yAxisMin = yAxisMin;
    }

    public String getControlLimitUsage() {
        return controlLimitUsage;
    }

    public void setControlLimitUsage(String controlLimitUsage) {
        this.controlLimitUsage = controlLimitUsage;
    }

    public BigDecimal getUpperControlLimit() {
        return upperControlLimit;
    }

    public void setUpperControlLimit(BigDecimal upperControlLimit) {
        this.upperControlLimit = upperControlLimit;
    }

    public BigDecimal getCenterLine() {
        return centerLine;
    }

    public void setCenterLine(BigDecimal centerLine) {
        this.centerLine = centerLine;
    }

    public BigDecimal getLowerControlLimit() {
        return lowerControlLimit;
    }

    public void setLowerControlLimit(BigDecimal lowerControlLimit) {
        this.lowerControlLimit = lowerControlLimit;
    }

    public String getDisplaySpecLimit() {
        return displaySpecLimit;
    }

    public void setDisplaySpecLimit(String displaySpecLimit) {
        this.displaySpecLimit = displaySpecLimit;
    }

    public BigDecimal getUpperSpecLimit() {
        return upperSpecLimit;
    }

    public void setUpperSpecLimit(BigDecimal upperSpecLimit) {
        this.upperSpecLimit = upperSpecLimit;
    }

    public BigDecimal getSpecTarget() {
        return specTarget;
    }

    public void setSpecTarget(BigDecimal specTarget) {
        this.specTarget = specTarget;
    }

    public BigDecimal getLowerSpecLimit() {
        return lowerSpecLimit;
    }

    public void setLowerSpecLimit(BigDecimal lowerSpecLimit) {
        this.lowerSpecLimit = lowerSpecLimit;
    }

    public String getEnableJudgeGroup() {
        return enableJudgeGroup;
    }

    public void setEnableJudgeGroup(String enableJudgeGroup) {
        this.enableJudgeGroup = enableJudgeGroup;
    }

    public Long getJudgementGroupId() {
        return judgementGroupId;
    }

    public void setJudgementGroupId(Long judgementGroupId) {
        this.judgementGroupId = judgementGroupId;
    }

    public String getJudgementGroupCode() {
        return judgementGroupCode;
    }

    public void setJudgementGroupCode(String judgementGroupCode) {
        this.judgementGroupCode = judgementGroupCode;
    }
}
