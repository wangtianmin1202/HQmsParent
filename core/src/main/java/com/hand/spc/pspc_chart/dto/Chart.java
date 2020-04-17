package com.hand.spc.pspc_chart.dto;

/**
 * Auto Generated By Hap Code Generator
 **/

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Table;
import javax.persistence.Transient;

import com.hand.hap.system.dto.BaseDTO;

@ExtensionAttribute(disable = true)
@Table(name = "PSPC_CHART")
public class Chart extends BaseDTO {

    public static final String FIELD_CHART_ID = "chartId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_CHART_CODE = "chartCode";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_CHART_TYPE = "chartType";
    public static final String FIELD_MULTI_MAP_SINGLE = "multiMapSingle";
    public static final String FIELD_MULTI_MAP_NORMALITY = "multiMapNormality";
    public static final String FIELD_MULTI_MAP_CP = "multiMapCp";
    public static final String FIELD_MULTI_MAP_PLATO = "multiMapPlato";
    public static final String FIELD_CHART_TITLE = "chartTitle";
    public static final String FIELD_TRIGGER_MESSAGE = "triggerMessage";
    public static final String FIELD_SUBGROUP_SIZE = "subgroupSize";
    public static final String FIELD_MAX_PLOT_POINTS = "maxPlotPoints";
    public static final String FIELD_X_TICK_LABEL = "xTickLabel";

    @Id
    @GeneratedValue
    private Float chartId;

    private Float tenantId;

    private Float siteId;

    @Length(max = 30)
    private String chartCode;

    @Length(max = 200)
    private String description;

    @Length(max = 30)
    private String chartType;

    @Length(max = 10)
    private String multiMapSingle;

    @Length(max = 10)
    private String multiMapNormality;

    @Length(max = 10)
    private String multiMapCp;

    @Length(max = 10)
    private String multiMapPlato;

    @Length(max = 100)
    private String chartTitle;

    @Length(max = 100)
    private String triggerMessage;

    private Float subgroupSize;

    private Float maxPlotPoints;

    @Length(max = 100)
    private String tickLabelX;

    @Transient
    private Float chartTypeControl;

    @Transient
    private String controlChartType;

    @Transient
    private String enableFlagSub;

    @Transient
    private String enableFlagType;

    public String getEnableFlagSub() {
        return enableFlagSub;
    }

    public void setEnableFlagSub(String enableFlagSub) {
        this.enableFlagSub = enableFlagSub;
    }

    public String getEnableFlagType() {
        return enableFlagType;
    }

    public void setEnableFlagType(String enableFlagType) {
        this.enableFlagType = enableFlagType;
    }

    public String getTickLabelX() {
        return tickLabelX;
    }

    public void setTickLabelX(String tickLabelX) {
        this.tickLabelX = tickLabelX;
    }

    public String getControlChartType() {
        return controlChartType;
    }

    public void setControlChartType(String controlChartType) {
        this.controlChartType = controlChartType;
    }

    public Float getChartTypeControl() {
        return chartTypeControl;
    }

    public void setChartTypeControl(Float chartTypeControl) {
        this.chartTypeControl = chartTypeControl;
    }

    public void setChartId(Float chartId) {
        this.chartId = chartId;
    }

    public Float getChartId() {
        return chartId;
    }

    public void setTenantId(Float tenantId) {
        this.tenantId = tenantId;
    }

    public Float getTenantId() {
        return tenantId;
    }

    public void setSiteId(Float siteId) {
        this.siteId = siteId;
    }

    public Float getSiteId() {
        return siteId;
    }

    public void setChartCode(String chartCode) {
        this.chartCode = chartCode;
    }

    public String getChartCode() {
        return chartCode;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public String getChartType() {
        return chartType;
    }

    public void setMultiMapSingle(String multiMapSingle) {
        this.multiMapSingle = multiMapSingle;
    }

    public String getMultiMapSingle() {
        return multiMapSingle;
    }

    public void setMultiMapNormality(String multiMapNormality) {
        this.multiMapNormality = multiMapNormality;
    }

    public String getMultiMapNormality() {
        return multiMapNormality;
    }

    public void setMultiMapCp(String multiMapCp) {
        this.multiMapCp = multiMapCp;
    }

    public String getMultiMapCp() {
        return multiMapCp;
    }

    public void setMultiMapPlato(String multiMapPlato) {
        this.multiMapPlato = multiMapPlato;
    }

    public String getMultiMapPlato() {
        return multiMapPlato;
    }

    public void setChartTitle(String chartTitle) {
        this.chartTitle = chartTitle;
    }

    public String getChartTitle() {
        return chartTitle;
    }

    public void setTriggerMessage(String triggerMessage) {
        this.triggerMessage = triggerMessage;
    }

    public String getTriggerMessage() {
        return triggerMessage;
    }

    public void setSubgroupSize(Float subgroupSize) {
        this.subgroupSize = subgroupSize;
    }

    public Float getSubgroupSize() {
        return subgroupSize;
    }

    public void setMaxPlotPoints(Float maxPlotPoints) {
        this.maxPlotPoints = maxPlotPoints;
    }

    public Float getMaxPlotPoints() {
        return maxPlotPoints;
    }


}