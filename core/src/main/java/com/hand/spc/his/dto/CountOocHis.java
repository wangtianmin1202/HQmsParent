package com.hand.spc.his.dto;

/**
 * Auto Generated By Hap Code Generator
 **/

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Table;

import com.hand.hap.system.dto.BaseDTO;

@ExtensionAttribute(disable = true)
@Table(name = "PSPC_COUNT_OOC_HIS")
public class CountOocHis extends BaseDTO {

    public static final String FIELD_COUNT_OOC_HIS_ID = "countOocHisId";
    public static final String FIELD_COUNT_OOC_ID = "countOocId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_OOC_STATUS = "oocStatus";
    public static final String FIELD_ENTITY_CODE = "entityCode";
    public static final String FIELD_ENTITY_VERSION = "entityVersion";
    public static final String FIELD_MAX_PLOT_POINTS = "maxPlotPoints";
    public static final String FIELD_X_TICK_LABEL = "xTickLabel";
    public static final String FIELD_X_AXIS_LABEL = "xAxisLabel";
    public static final String FIELD_Y_AXIS_LABEL = "yAxisLabel";
    public static final String FIELD_CHART_DETAIL_TYPE = "chartDetailType";
    public static final String FIELD_UPPER_CONTROL_LIMIT = "upperControlLimit";
    public static final String FIELD_CENTER_LINE = "centerLine";
    public static final String FIELD_LOWER_CONTROL_LIMIT = "lowerControlLimit";
    public static final String FIELD_UPPER_SPEC_LIMIT = "upperSpecLimit";
    public static final String FIELD_SPEC_TARGET = "specTarget";
    public static final String FIELD_LOWER_SPEC_LIMIT = "lowerSpecLimit";
    public static final String FIELD_JUDGEMENT_ID = "judgementId";
    public static final String FIELD_FIRST_SUBGROUP_NUM = "firstSubgroupNum";
    public static final String FIELD_LAST_SUBGROUP_NUM = "lastSubgroupNum";
    public static final String FIELD_CLASSIFY_GROUP_ID = "classifyGroupId";
    public static final String FIELD_CLASSIFY_ID = "classifyId";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_COUNT_SAMPLE_DATA_ID = "countSampleDataId";

    @Id
    @GeneratedValue
    @Length(max = 100)
    private String countOocHisId;

    @Length(max = 100)
    private String countOocId;

    private Float tenantId;

    private Float siteId;

    @Length(max = 30)
    private String oocStatus;

    @Length(max = 30)
    private String entityCode;

    @Length(max = 30)
    private String entityVersion;

    private Float maxPlotPoints;

    @Length(max = 100)
    private String tickLabelX;

    @Length(max = 100)
    private String axisLabelX;

    @Length(max = 100)
    private String axisLabelY;

    @Length(max = 30)
    private String chartDetailType;

    private Float upperControlLimit;

    private Float centerLine;

    private Float lowerControlLimit;

    private Float upperSpecLimit;

    private Float specTarget;

    private Float lowerSpecLimit;

    private Float judgementId;

    private Float firstSubgroupNum;

    private Float lastSubgroupNum;

    private Float classifyGroupId;

    private Float classifyId;

    @Length(max = 1000)
    private String remark;

    private Float countSampleDataId;


    public String getTickLabelX() {
        return tickLabelX;
    }

    public void setTickLabelX(String tickLabelX) {
        this.tickLabelX = tickLabelX;
    }

    public String getAxisLabelX() {
        return axisLabelX;
    }

    public void setAxisLabelX(String axisLabelX) {
        this.axisLabelX = axisLabelX;
    }

    public String getAxisLabelY() {
        return axisLabelY;
    }

    public void setAxisLabelY(String axisLabelY) {
        this.axisLabelY = axisLabelY;
    }

    public void setCountOocHisId(String countOocHisId) {
        this.countOocHisId = countOocHisId;
    }

    public String getCountOocHisId() {
        return countOocHisId;
    }

    public void setCountOocId(String countOocId) {
        this.countOocId = countOocId;
    }

    public String getCountOocId() {
        return countOocId;
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

    public void setOocStatus(String oocStatus) {
        this.oocStatus = oocStatus;
    }

    public String getOocStatus() {
        return oocStatus;
    }

    public void setEntityCode(String entityCode) {
        this.entityCode = entityCode;
    }

    public String getEntityCode() {
        return entityCode;
    }

    public void setEntityVersion(String entityVersion) {
        this.entityVersion = entityVersion;
    }

    public String getEntityVersion() {
        return entityVersion;
    }

    public void setMaxPlotPoints(Float maxPlotPoints) {
        this.maxPlotPoints = maxPlotPoints;
    }

    public Float getMaxPlotPoints() {
        return maxPlotPoints;
    }

    public void setChartDetailType(String chartDetailType) {
        this.chartDetailType = chartDetailType;
    }

    public String getChartDetailType() {
        return chartDetailType;
    }

    public void setUpperControlLimit(Float upperControlLimit) {
        this.upperControlLimit = upperControlLimit;
    }

    public Float getUpperControlLimit() {
        return upperControlLimit;
    }

    public void setCenterLine(Float centerLine) {
        this.centerLine = centerLine;
    }

    public Float getCenterLine() {
        return centerLine;
    }

    public void setLowerControlLimit(Float lowerControlLimit) {
        this.lowerControlLimit = lowerControlLimit;
    }

    public Float getLowerControlLimit() {
        return lowerControlLimit;
    }

    public void setUpperSpecLimit(Float upperSpecLimit) {
        this.upperSpecLimit = upperSpecLimit;
    }

    public Float getUpperSpecLimit() {
        return upperSpecLimit;
    }

    public void setSpecTarget(Float specTarget) {
        this.specTarget = specTarget;
    }

    public Float getSpecTarget() {
        return specTarget;
    }

    public void setLowerSpecLimit(Float lowerSpecLimit) {
        this.lowerSpecLimit = lowerSpecLimit;
    }

    public Float getLowerSpecLimit() {
        return lowerSpecLimit;
    }

    public void setJudgementId(Float judgementId) {
        this.judgementId = judgementId;
    }

    public Float getJudgementId() {
        return judgementId;
    }

    public void setFirstSubgroupNum(Float firstSubgroupNum) {
        this.firstSubgroupNum = firstSubgroupNum;
    }

    public Float getFirstSubgroupNum() {
        return firstSubgroupNum;
    }

    public void setLastSubgroupNum(Float lastSubgroupNum) {
        this.lastSubgroupNum = lastSubgroupNum;
    }

    public Float getLastSubgroupNum() {
        return lastSubgroupNum;
    }

    public void setClassifyGroupId(Float classifyGroupId) {
        this.classifyGroupId = classifyGroupId;
    }

    public Float getClassifyGroupId() {
        return classifyGroupId;
    }

    public void setClassifyId(Float classifyId) {
        this.classifyId = classifyId;
    }

    public Float getClassifyId() {
        return classifyId;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public void setCountSampleDataId(Float countSampleDataId) {
        this.countSampleDataId = countSampleDataId;
    }

    public Float getCountSampleDataId() {
        return countSampleDataId;
    }

}
