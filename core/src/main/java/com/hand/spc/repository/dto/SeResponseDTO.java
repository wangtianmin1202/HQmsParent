package com.hand.spc.repository.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.system.dto.BaseDTO;

import io.swagger.annotations.ApiModelProperty;

/**
 * SE图形查询DTO
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SeResponseDTO extends BaseDTO{
    @ApiModelProperty(value = "控制图类型")
    private String chartType;
    @ApiModelProperty(value = "控制图标题")
    private String chartTitle;
    @ApiModelProperty(value = "子组大小")
    private Long subgroupSize;
    @ApiModelProperty(value = "SE点数据")
    private List<SePointDataDTO> sePointDataDTOList;
    @ApiModelProperty(value = "SE点数据(计数)")
    private List<SeCountPointDataDTO> seCountPointDataDTOList;
    @ApiModelProperty(value = "主图X轴标签")
    private String mainXAxisLabel;
    @ApiModelProperty(value = "主图Y轴标签")
    private String mainYAxisLabel;
    @ApiModelProperty(value = "主图Y轴最大值")
    private Long mainYAxisMax;
    @ApiModelProperty(value = "主图Y轴最小值")
    private Long mainYAxisMin;
    @ApiModelProperty(value = "主图整体标准差")
    private BigDecimal mainEntiretySigma;
    @ApiModelProperty(value = "主图整体规格上限")
    private BigDecimal mainEntiretyUsl;
    @ApiModelProperty(value = "主图整体规格下限")
    private BigDecimal mainEntiretyLsl;
    @ApiModelProperty(value = "主图整体控制上限")
    private BigDecimal mainEntiretyUcl;
    @ApiModelProperty(value = "主图整体控制中心线")
    private BigDecimal mainEntiretyCl;
    @ApiModelProperty(value = "主图整体控制下限")
    private BigDecimal mainEntiretyLcl;
    @ApiModelProperty(value = "次图X轴标签")
    private String secondXAxisLabel;
    @ApiModelProperty(value = "次图Y轴标签")
    private String secondYAxisLabel;
    @ApiModelProperty(value = "次图Y轴最大值")
    private Long secondYAxisMax;
    @ApiModelProperty(value = "次图Y轴最小值")
    private Long secondYAxisMin;
    @ApiModelProperty(value = "次图整体标准差")
    private BigDecimal secondEntiretySigma;
    @ApiModelProperty(value = "次图整体规格上限")
    private BigDecimal secondEntiretyUsl;
    @ApiModelProperty(value = "次图整体规格下限")
    private BigDecimal secondEntiretyLsl;
    @ApiModelProperty(value = "次图整体控制上限")
    private BigDecimal secondEntiretyUcl;
    @ApiModelProperty(value = "次图整体控制中心线")
    private BigDecimal secondEntiretyCl;
    @ApiModelProperty(value = "次图整体控制下限")
    private BigDecimal secondEntiretyLcl;

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public String getChartTitle() {
        return chartTitle;
    }

    public void setChartTitle(String chartTitle) {
        this.chartTitle = chartTitle;
    }

    public Long getSubgroupSize() {
        return subgroupSize;
    }

    public void setSubgroupSize(Long subgroupSize) {
        this.subgroupSize = subgroupSize;
    }

    public List<SePointDataDTO> getSePointDataDTOList() {
        return sePointDataDTOList;
    }

    public void setSePointDataDTOList(List<SePointDataDTO> sePointDataDTOList) {
        this.sePointDataDTOList = sePointDataDTOList;
    }

    public String getMainXAxisLabel() {
        return mainXAxisLabel;
    }

    public void setMainXAxisLabel(String mainXAxisLabel) {
        this.mainXAxisLabel = mainXAxisLabel;
    }

    public String getMainYAxisLabel() {
        return mainYAxisLabel;
    }

    public void setMainYAxisLabel(String mainYAxisLabel) {
        this.mainYAxisLabel = mainYAxisLabel;
    }

    public Long getMainYAxisMax() {
        return mainYAxisMax;
    }

    public void setMainYAxisMax(Long mainYAxisMax) {
        this.mainYAxisMax = mainYAxisMax;
    }

    public Long getMainYAxisMin() {
        return mainYAxisMin;
    }

    public void setMainYAxisMin(Long mainYAxisMin) {
        this.mainYAxisMin = mainYAxisMin;
    }

    public BigDecimal getMainEntiretySigma() {
        return mainEntiretySigma;
    }

    public void setMainEntiretySigma(BigDecimal mainEntiretySigma) {
        this.mainEntiretySigma = mainEntiretySigma;
    }

    public BigDecimal getMainEntiretyUsl() {
        return mainEntiretyUsl;
    }

    public void setMainEntiretyUsl(BigDecimal mainEntiretyUsl) {
        this.mainEntiretyUsl = mainEntiretyUsl;
    }

    public BigDecimal getMainEntiretyLsl() {
        return mainEntiretyLsl;
    }

    public void setMainEntiretyLsl(BigDecimal mainEntiretyLsl) {
        this.mainEntiretyLsl = mainEntiretyLsl;
    }

    public BigDecimal getMainEntiretyUcl() {
        return mainEntiretyUcl;
    }

    public void setMainEntiretyUcl(BigDecimal mainEntiretyUcl) {
        this.mainEntiretyUcl = mainEntiretyUcl;
    }

    public BigDecimal getMainEntiretyCl() {
        return mainEntiretyCl;
    }

    public void setMainEntiretyCl(BigDecimal mainEntiretyCl) {
        this.mainEntiretyCl = mainEntiretyCl;
    }

    public BigDecimal getMainEntiretyLcl() {
        return mainEntiretyLcl;
    }

    public void setMainEntiretyLcl(BigDecimal mainEntiretyLcl) {
        this.mainEntiretyLcl = mainEntiretyLcl;
    }

    public String getSecondXAxisLabel() {
        return secondXAxisLabel;
    }

    public void setSecondXAxisLabel(String secondXAxisLabel) {
        this.secondXAxisLabel = secondXAxisLabel;
    }

    public String getSecondYAxisLabel() {
        return secondYAxisLabel;
    }

    public void setSecondYAxisLabel(String secondYAxisLabel) {
        this.secondYAxisLabel = secondYAxisLabel;
    }

    public Long getSecondYAxisMax() {
        return secondYAxisMax;
    }

    public void setSecondYAxisMax(Long secondYAxisMax) {
        this.secondYAxisMax = secondYAxisMax;
    }

    public Long getSecondYAxisMin() {
        return secondYAxisMin;
    }

    public void setSecondYAxisMin(Long secondYAxisMin) {
        this.secondYAxisMin = secondYAxisMin;
    }

    public BigDecimal getSecondEntiretySigma() {
        return secondEntiretySigma;
    }

    public void setSecondEntiretySigma(BigDecimal secondEntiretySigma) {
        this.secondEntiretySigma = secondEntiretySigma;
    }

    public BigDecimal getSecondEntiretyUsl() {
        return secondEntiretyUsl;
    }

    public void setSecondEntiretyUsl(BigDecimal secondEntiretyUsl) {
        this.secondEntiretyUsl = secondEntiretyUsl;
    }

    public BigDecimal getSecondEntiretyLsl() {
        return secondEntiretyLsl;
    }

    public void setSecondEntiretyLsl(BigDecimal secondEntiretyLsl) {
        this.secondEntiretyLsl = secondEntiretyLsl;
    }

    public BigDecimal getSecondEntiretyUcl() {
        return secondEntiretyUcl;
    }

    public void setSecondEntiretyUcl(BigDecimal secondEntiretyUcl) {
        this.secondEntiretyUcl = secondEntiretyUcl;
    }

    public BigDecimal getSecondEntiretyCl() {
        return secondEntiretyCl;
    }

    public void setSecondEntiretyCl(BigDecimal secondEntiretyCl) {
        this.secondEntiretyCl = secondEntiretyCl;
    }

    public BigDecimal getSecondEntiretyLcl() {
        return secondEntiretyLcl;
    }

    public void setSecondEntiretyLcl(BigDecimal secondEntiretyLcl) {
        this.secondEntiretyLcl = secondEntiretyLcl;
    }

    public List<SeCountPointDataDTO> getSeCountPointDataDTOList() {
        return seCountPointDataDTOList;
    }

    public void setSeCountPointDataDTOList(List<SeCountPointDataDTO> seCountPointDataDTOList) {
        this.seCountPointDataDTOList = seCountPointDataDTOList;
    }
}
