package com.hand.spc.pspc_cpk_analysis.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CPKAnalyseResDTO {

    @ApiModelProperty("规格上限")
    private BigDecimal upperSpecLimit;
    @ApiModelProperty("规格下限")
    private BigDecimal 	lowerSpecLimit;


    @ApiModelProperty("cpk分析图")
    private CPKAnalyseChartVo cpkAnalyseChartVo;
    @ApiModelProperty("cpk列表")
    private CPKAnalyseTableVo cpkAnalyseTableVo;
    @ApiModelProperty("过程数据")
    private ProcessDataVo processDataVo;
    @ApiModelProperty("整体能力")
    private OverallAbilityVo overallAbilityVo;
    @ApiModelProperty("潜在能力")
    private PotentialAbilityVo potentialAbilityVo;
    /**
     * 横坐标：样本组距中点
     */
    private List<BigDecimal> xList;
    /**
     * 频数
     */
    private List<BigDecimal> frequency;
    /**
     * 组内概率
     */
    private List<String> intraGroupProbability;
    /**
     * 整体概率
     */
    private List<String> overallProbability;

    public List<BigDecimal> getxList() {
        return xList;
    }

    public void setxList(List<BigDecimal> xList) {
        this.xList = xList;
    }

    public List<BigDecimal> getFrequency() {
        return frequency;
    }

    public void setFrequency(List<BigDecimal> frequency) {
        this.frequency = frequency;
    }

    public List<String> getIntraGroupProbability() {
        return intraGroupProbability;
    }

    public void setIntraGroupProbability(List<String> intraGroupProbability) {
        this.intraGroupProbability = intraGroupProbability;
    }

    public List<String> getOverallProbability() {
        return overallProbability;
    }

    public void setOverallProbability(List<String> overallProbability) {
        this.overallProbability = overallProbability;
    }

    public BigDecimal getUpperSpecLimit() {
        return upperSpecLimit;
    }

    public void setUpperSpecLimit(BigDecimal upperSpecLimit) {
        this.upperSpecLimit = upperSpecLimit;
    }

    public BigDecimal getLowerSpecLimit() {
        return lowerSpecLimit;
    }

    public void setLowerSpecLimit(BigDecimal lowerSpecLimit) {
        this.lowerSpecLimit = lowerSpecLimit;
    }

    public CPKAnalyseTableVo getCpkAnalyseTableVo() {
        return cpkAnalyseTableVo;
    }

    public void setCpkAnalyseTableVo(CPKAnalyseTableVo cpkAnalyseTableVo) {
        this.cpkAnalyseTableVo = cpkAnalyseTableVo;
    }

    public ProcessDataVo getProcessDataVo() {
        return processDataVo;
    }

    public void setProcessDataVo(ProcessDataVo processDataVo) {
        this.processDataVo = processDataVo;
    }

    public OverallAbilityVo getOverallAbilityVo() {
        return overallAbilityVo;
    }

    public void setOverallAbilityVo(OverallAbilityVo overallAbilityVo) {
        this.overallAbilityVo = overallAbilityVo;
    }

    public PotentialAbilityVo getPotentialAbilityVo() {
        return potentialAbilityVo;
    }

    public void setPotentialAbilityVo(PotentialAbilityVo potentialAbilityVo) {
        this.potentialAbilityVo = potentialAbilityVo;
    }

    public CPKAnalyseChartVo getCpkAnalyseChartVo() {
        return cpkAnalyseChartVo;
    }

    public void setCpkAnalyseChartVo(CPKAnalyseChartVo cpkAnalyseChartVo) {
        this.cpkAnalyseChartVo = cpkAnalyseChartVo;
    }
}
