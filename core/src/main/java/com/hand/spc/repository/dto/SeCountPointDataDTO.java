package com.hand.spc.repository.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.system.dto.BaseDTO;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SeCountPointDataDTO extends BaseDTO{
    @ApiModelProperty(value = "主键ID")
    private Long countStatisticId;
    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
    @ApiModelProperty(value = "站点ID")
    private Long siteId;
    @ApiModelProperty(value = "样本数据ID")
    private Long countSampleDataId;
    @ApiModelProperty(value = "组号")
    private Long subgroupNum;
    @ApiModelProperty(value = "样本时间")
    private Date sampleTime;
    @ApiModelProperty(value = "样本数（计数)")
    private BigDecimal sampleValueCount;
    @ApiModelProperty(value = "不合格数")
    private BigDecimal unqualifiedQuantity;
    @ApiModelProperty(value = "不合格率/单位缺陷数")
    private BigDecimal unqualifiedPercent;
    @ApiModelProperty(value = "控制上限")
    private BigDecimal upperControlLimit;
    @ApiModelProperty(value = "中心线")
    private BigDecimal centerLine;
    @ApiModelProperty(value = "控制下限")
    private BigDecimal lowerControlLimit;
    @ApiModelProperty(value = "规格上限")
    private BigDecimal upperSpecLimit;
    @ApiModelProperty(value = "目标值")
    private BigDecimal specTarget;
    @ApiModelProperty(value = "规格下限")
    private BigDecimal lowerSpecLimit;
    @ApiModelProperty("分类项行表(计数)对象")
    private List<SeCountSampleDataClassifyVO> seCountSampleDataClassifyDTOList;
    @ApiModelProperty("样本数据拓展表(计数)对象")
    private List<SeCountSampleDataExtendVO> seCountSampleDataExtendDTOList;
    @ApiModelProperty("SIGMA线")
    private BigDecimal sigmaLine;
    @ApiModelProperty("X轴刻度标签")
    private String xTickLabel;
    @ApiModelProperty("状态")
    private String status;//逻辑计算(COUNT OOC)
    @ApiModelProperty("COUNT OOC集合")
    private List<CountOocResponseDTO> countOocResponseDTOList;

    public Long getCountStatisticId() {
        return countStatisticId;
    }

    public void setCountStatisticId(Long countStatisticId) {
        this.countStatisticId = countStatisticId;
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

    public Long getCountSampleDataId() {
        return countSampleDataId;
    }

    public void setCountSampleDataId(Long countSampleDataId) {
        this.countSampleDataId = countSampleDataId;
    }

    public Long getSubgroupNum() {
        return subgroupNum;
    }

    public void setSubgroupNum(Long subgroupNum) {
        this.subgroupNum = subgroupNum;
    }

    public Date getSampleTime() {
        return sampleTime;
    }

    public void setSampleTime(Date sampleTime) {
        this.sampleTime = sampleTime;
    }

    public BigDecimal getSampleValueCount() {
        return sampleValueCount;
    }

    public void setSampleValueCount(BigDecimal sampleValueCount) {
        this.sampleValueCount = sampleValueCount;
    }

    public BigDecimal getUnqualifiedQuantity() {
        return unqualifiedQuantity;
    }

    public void setUnqualifiedQuantity(BigDecimal unqualifiedQuantity) {
        this.unqualifiedQuantity = unqualifiedQuantity;
    }

    public BigDecimal getUnqualifiedPercent() {
        return unqualifiedPercent;
    }

    public void setUnqualifiedPercent(BigDecimal unqualifiedPercent) {
        this.unqualifiedPercent = unqualifiedPercent;
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

    public List<SeCountSampleDataClassifyVO> getSeCountSampleDataClassifyDTOList() {
        return seCountSampleDataClassifyDTOList;
    }

    public void setSeCountSampleDataClassifyDTOList(List<SeCountSampleDataClassifyVO> seCountSampleDataClassifyDTOList) {
        this.seCountSampleDataClassifyDTOList = seCountSampleDataClassifyDTOList;
    }

    public List<SeCountSampleDataExtendVO> getSeCountSampleDataExtendDTOList() {
        return seCountSampleDataExtendDTOList;
    }

    public void setSeCountSampleDataExtendDTOList(List<SeCountSampleDataExtendVO> seCountSampleDataExtendDTOList) {
        this.seCountSampleDataExtendDTOList = seCountSampleDataExtendDTOList;
    }

    public BigDecimal getSigmaLine() {
        return sigmaLine;
    }

    public void setSigmaLine(BigDecimal sigmaLine) {
        this.sigmaLine = sigmaLine;
    }

    public String getxTickLabel() {
        return xTickLabel;
    }

    public void setxTickLabel(String xTickLabel) {
        this.xTickLabel = xTickLabel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CountOocResponseDTO> getCountOocResponseDTOList() {
        return countOocResponseDTOList;
    }

    public void setCountOocResponseDTOList(List<CountOocResponseDTO> countOocResponseDTOList) {
        this.countOocResponseDTOList = countOocResponseDTOList;
    }
}
