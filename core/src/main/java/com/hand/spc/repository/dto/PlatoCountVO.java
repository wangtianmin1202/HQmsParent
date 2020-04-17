package com.hand.spc.repository.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * 柏拉图 返回对象
 */
public class PlatoCountVO {
    @ApiModelProperty(value = "表ID，主键")
    private Long countStatisticId;
    @ApiModelProperty(value = "样本数据ID")
    private Long countSampleDataId;
    @ApiModelProperty(value = "站点ID")
    private Long tenantId;
    @ApiModelProperty(value = "站点ID")
    private Long siteId;
    @ApiModelProperty(value = "样本时间")
    private Date sampleTime;
    @ApiModelProperty(value = "样本数(计数)")
    private BigDecimal sampleValueCount;
    @ApiModelProperty(value = "不合格数")
    private BigDecimal unqualifiedQuantity;
    @ApiModelProperty(value = "不合格率/单位缺陷数")
    private BigDecimal unqualifiedPercent;
    @ApiModelProperty("分类项行表集合(计数)")
    private List<PlatoCountSampleDataClassifyVO> platoCountSampleDataClassifyDTOList;
    @ApiModelProperty("样本数据拓展表集合(计数)")
    private List<PlatoCountSampleDataExtendVO> platoCountSampleDataExtendDTOList;
    @ApiModelProperty(value = "COUNT OOC状态")
    private String oocStatus;//逻辑计算获取

    public Long getCountStatisticId() {
        return countStatisticId;
    }

    public void setCountStatisticId(Long countStatisticId) {
        this.countStatisticId = countStatisticId;
    }

    public Long getCountSampleDataId() {
        return countSampleDataId;
    }

    public void setCountSampleDataId(Long countSampleDataId) {
        this.countSampleDataId = countSampleDataId;
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

    public String getOocStatus() {
        return oocStatus;
    }

    public void setOocStatus(String oocStatus) {
        this.oocStatus = oocStatus;
    }

    public List<PlatoCountSampleDataClassifyVO> getPlatoCountSampleDataClassifyDTOList() {
        return platoCountSampleDataClassifyDTOList;
    }

    public void setPlatoCountSampleDataClassifyDTOList(List<PlatoCountSampleDataClassifyVO> platoCountSampleDataClassifyDTOList) {
        this.platoCountSampleDataClassifyDTOList = platoCountSampleDataClassifyDTOList;
    }

    public List<PlatoCountSampleDataExtendVO> getPlatoCountSampleDataExtendDTOList() {
        return platoCountSampleDataExtendDTOList;
    }

    public void setPlatoCountSampleDataExtendDTOList(List<PlatoCountSampleDataExtendVO> platoCountSampleDataExtendDTOList) {
        this.platoCountSampleDataExtendDTOList = platoCountSampleDataExtendDTOList;
    }
}
