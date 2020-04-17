package com.hand.spc.pspc_cpk_analysis.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * 过程数据
 */
public class ProcessDataVo {

    private BigDecimal upperSpecLimit;//规格上限
    private BigDecimal 	lowerControlLimit;//规格下限
    private BigDecimal 	specTarget;//	目标
    private BigDecimal 	sampleMean;//	样本均值
    private BigDecimal 	standardDeviationOver;//	标准差（整体）
    private BigDecimal 	standardDeviationIn;//	标准差（整内）
    private BigDecimal 	sampleN;//	样本N
    private int maxSampleCount;//样本组里的最大样本数

    public int getMaxSampleCount() {
        return maxSampleCount;
    }

    public void setMaxSampleCount(int maxSampleCount) {
        this.maxSampleCount = maxSampleCount;
    }

    private CPKAnalyseTableVo cpkAnalyseTableVo;

    private CPKAnalyseChartVo cpkAnalyseChartVo;

    public CPKAnalyseChartVo getCpkAnalyseChartVo() {
        return cpkAnalyseChartVo;
    }

    public void setCpkAnalyseChartVo(CPKAnalyseChartVo cpkAnalyseChartVo) {
        this.cpkAnalyseChartVo = cpkAnalyseChartVo;
    }

    public CPKAnalyseTableVo getCpkAnalyseTableVo() {
        return cpkAnalyseTableVo;
    }

    public void setCpkAnalyseTableVo(CPKAnalyseTableVo cpkAnalyseTableVo) {
        this.cpkAnalyseTableVo = cpkAnalyseTableVo;
    }

    private List<BigDecimal> sampleValues;//样本值List

    public List<BigDecimal> getSampleValues() {
        return sampleValues;
    }

    public void setSampleValues(List<BigDecimal> sampleValues) {
        this.sampleValues = sampleValues;
    }

    public BigDecimal getUpperSpecLimit() {
        return upperSpecLimit;
    }

    public void setUpperSpecLimit(BigDecimal upperSpecLimit) {
        this.upperSpecLimit = upperSpecLimit;
    }

    public BigDecimal getLowerControlLimit() {
        return lowerControlLimit;
    }

    public void setLowerControlLimit(BigDecimal lowerControlLimit) {
        this.lowerControlLimit = lowerControlLimit;
    }

    public BigDecimal getSpecTarget() {
        return specTarget;
    }

    public void setSpecTarget(BigDecimal specTarget) {
        this.specTarget = specTarget;
    }

    public BigDecimal getSampleMean() {
        return sampleMean;
    }

    public void setSampleMean(BigDecimal sampleMean) {
        this.sampleMean = sampleMean;
    }

    public BigDecimal getStandardDeviationOver() {
        return standardDeviationOver;
    }

    public void setStandardDeviationOver(BigDecimal standardDeviationOver) {
        this.standardDeviationOver = standardDeviationOver;
    }

    public BigDecimal getStandardDeviationIn() {
        return standardDeviationIn;
    }

    public void setStandardDeviationIn(BigDecimal standardDeviationIn) {
        this.standardDeviationIn = standardDeviationIn;
    }

    public BigDecimal getSampleN() {
        return sampleN;
    }

    public void setSampleN(BigDecimal sampleN) {
        this.sampleN = sampleN;
    }
}
