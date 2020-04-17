package com.hand.spc.repository.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.system.dto.BaseDTO;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SePointDataDTO extends BaseDTO{

    @ApiModelProperty("样本分组ID")
    private Long sampleSubgroupId;//分组ID
    @ApiModelProperty("组号")
    private Long subgroupNum;//组号
    @ApiModelProperty("X轴刻度标签")
    private String xTickLabel;//X轴刻度标签
    @ApiModelProperty("平均值")
    private BigDecimal subgroupBar;//平均值
    @ApiModelProperty("极差")
    private BigDecimal subgroupR;//极差
    @ApiModelProperty("最大值")
    private BigDecimal subgroupMax;//最大值
    @ApiModelProperty("最小值")
    private BigDecimal subgroupMin;//最小值
    @ApiModelProperty("标准差")
    private BigDecimal subgroupSigma;//标准差
    @ApiModelProperty("中位数")
    private BigDecimal subgroupMe;//中位数
    @ApiModelProperty("移动极差")
    private BigDecimal subgroupRm;//移动极差
    @ApiModelProperty("样本时间")
    private Date sampleSubgroupTime;//样本时间
    @ApiModelProperty("状态")
    private String status;//状态
    @ApiModelProperty("样本数据集合")
    private List<SampleDataR> sampleDataList;//样本数据
    @ApiModelProperty("主图指标值")
    private BigDecimal mainStatisticValue;
    @ApiModelProperty("次图指标值")
    private BigDecimal secondStatisticValue;
    @ApiModelProperty("主图OOC集合")
    private List<OocResponseDTO> mainOocList;
    @ApiModelProperty("次图OOC集合")
    private List<OocResponseDTO> secondOocList;

    public Long getSampleSubgroupId() {
        return sampleSubgroupId;
    }

    public void setSampleSubgroupId(Long sampleSubgroupId) {
        this.sampleSubgroupId = sampleSubgroupId;
    }

    public Long getSubgroupNum() {
        return subgroupNum;
    }

    public void setSubgroupNum(Long subgroupNum) {
        this.subgroupNum = subgroupNum;
    }

    public String getxTickLabel() {
        return xTickLabel;
    }

    public void setxTickLabel(String xTickLabel) {
        this.xTickLabel = xTickLabel;
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

    public Date getSampleSubgroupTime() {
        return sampleSubgroupTime;
    }

    public void setSampleSubgroupTime(Date sampleSubgroupTime) {
        this.sampleSubgroupTime = sampleSubgroupTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<SampleDataR> getSampleDataList() {
        return sampleDataList;
    }

    public void setSampleDataList(List<SampleDataR> sampleDataList) {
        this.sampleDataList = sampleDataList;
    }

    public BigDecimal getMainStatisticValue() {
        return mainStatisticValue;
    }

    public void setMainStatisticValue(BigDecimal mainStatisticValue) {
        this.mainStatisticValue = mainStatisticValue;
    }

    public BigDecimal getSecondStatisticValue() {
        return secondStatisticValue;
    }

    public void setSecondStatisticValue(BigDecimal secondStatisticValue) {
        this.secondStatisticValue = secondStatisticValue;
    }

    public List<OocResponseDTO> getMainOocList() {
        return mainOocList;
    }

    public void setMainOocList(List<OocResponseDTO> mainOocList) {
        this.mainOocList = mainOocList;
    }

    public List<OocResponseDTO> getSecondOocList() {
        return secondOocList;
    }

    public void setSecondOocList(List<OocResponseDTO> secondOocList) {
        this.secondOocList = secondOocList;
    }
}
