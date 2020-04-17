package com.hand.spc.repository.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.system.dto.BaseDTO;
import io.swagger.annotations.ApiModel;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@ApiModel("OOC历史")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "pspc_ooc_his")
public class OocHisR extends BaseDTO {


    @Id
    @GeneratedValue
    private Long oocHisId;//ooc id
    private String oocId;//ooc id
    private Long tenantId;//租户ID
    private Long siteId;//站点id
    private String oocStatus;//OOC状态
    private Long sampleSubgroupId;//样本数据分组ID
    private String entityCode;//实体控制图编码
    private String entityVersion;//实体控制图版本
    private Long maxPlotPoints;//最大绘点数
    private String xTickLabel;//X轴刻度
    private String xAxisLabel;//X轴标签
    private String yAxisLabel;//Y轴标签
    private String chartDetailType;//控制图明细类型
    private BigDecimal upperControlLimit;//控制上限
    private BigDecimal centerLine;//中心线
    private BigDecimal lowerControlLimit;//控制下限
    private BigDecimal upperSpecLimit;//规格上限
    private BigDecimal specTarget;//目标值
    private BigDecimal lowerSpecLimit;//规格下限
    private Long judgementId;//判异规则ID
    private Long firstSubgroupNum;//开始样本数据分组组号
    private Long lastSubgroupNum;//结束样本数据分组组号
    private Long classifyGroupId;//分类组ID
    private Long classifyId;//分类项ID
    private String remark;//备注


    public String getOocId() {
        return oocId;
    }

    public void setOocId(String oocId) {
        this.oocId = oocId;
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

    public String getOocStatus() {
        return oocStatus;
    }

    public void setOocStatus(String oocStatus) {
        this.oocStatus = oocStatus;
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

    public Long getMaxPlotPoints() {
        return maxPlotPoints;
    }

    public void setMaxPlotPoints(Long maxPlotPoints) {
        this.maxPlotPoints = maxPlotPoints;
    }

    public String getxTickLabel() {
        return xTickLabel;
    }

    public void setxTickLabel(String xTickLabel) {
        this.xTickLabel = xTickLabel;
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

    public String getChartDetailType() {
        return chartDetailType;
    }

    public void setChartDetailType(String chartDetailType) {
        this.chartDetailType = chartDetailType;
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

    public Long getJudgementId() {
        return judgementId;
    }

    public void setJudgementId(Long judgementId) {
        this.judgementId = judgementId;
    }

    public Long getFirstSubgroupNum() {
        return firstSubgroupNum;
    }

    public void setFirstSubgroupNum(Long firstSubgroupNum) {
        this.firstSubgroupNum = firstSubgroupNum;
    }

    public Long getLastSubgroupNum() {
        return lastSubgroupNum;
    }

    public void setLastSubgroupNum(Long lastSubgroupNum) {
        this.lastSubgroupNum = lastSubgroupNum;
    }

    public Long getClassifyGroupId() {
        return classifyGroupId;
    }

    public void setClassifyGroupId(Long classifyGroupId) {
        this.classifyGroupId = classifyGroupId;
    }

    public Long getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(Long classifyId) {
        this.classifyId = classifyId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getOocHisId() {
        return oocHisId;
    }

    public void setOocHisId(Long oocHisId) {
        this.oocHisId = oocHisId;
    }

}
