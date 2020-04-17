package com.hand.spc.repository.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * 实体控制图&控制图关联
 */
public class EntityByChartVO {
    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
    @ApiModelProperty(value = "站点ID")
    private Long siteId;
    @ApiModelProperty(value = "实体控制图")
    private String entityCode;
    @ApiModelProperty(value = "实体控制图版本")
    private String entityVersion;
    @ApiModelProperty(value = "控制图代码")
    private String chartCode;
    @ApiModelProperty(value = "控制图类型")
    private String chartType;
    @ApiModelProperty(value = "最多绘点数")
    private Long maxPlotPoints;

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

    public String getChartCode() {
        return chartCode;
    }

    public void setChartCode(String chartCode) {
        this.chartCode = chartCode;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public Long getMaxPlotPoints() {
        return maxPlotPoints;
    }

    public void setMaxPlotPoints(Long maxPlotPoints) {
        this.maxPlotPoints = maxPlotPoints;
    }
}
