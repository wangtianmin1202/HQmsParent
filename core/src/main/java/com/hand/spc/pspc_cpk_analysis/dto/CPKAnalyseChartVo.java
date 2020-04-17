package com.hand.spc.pspc_cpk_analysis.dto;

import java.math.BigDecimal;
import java.util.List;

public class CPKAnalyseChartVo {


    private List<CPKAnalyseChartDataVo> cpkAnalyseChartDataVo;

    private BigDecimal distanceI;//组距
    private BigDecimal samplen;//柱子数

    private BigDecimal entiretyUcl;//整体控制上限
    private BigDecimal entiretyCl;//整体中心线
    private BigDecimal entiretyLcl;//整体控制下限


    public BigDecimal getEntiretyUcl() {
        return entiretyUcl;
    }

    public void setEntiretyUcl(BigDecimal entiretyUcl) {
        this.entiretyUcl = entiretyUcl;
    }

    public BigDecimal getEntiretyCl() {
        return entiretyCl;
    }

    public void setEntiretyCl(BigDecimal entiretyCl) {
        this.entiretyCl = entiretyCl;
    }

    public BigDecimal getEntiretyLcl() {
        return entiretyLcl;
    }

    public void setEntiretyLcl(BigDecimal entiretyLcl) {
        this.entiretyLcl = entiretyLcl;
    }

    public BigDecimal getDistanceI() {
        return distanceI;
    }

    public void setDistanceI(BigDecimal distanceI) {
        this.distanceI = distanceI;
    }

    public BigDecimal getSamplen() {
        return samplen;
    }

    public void setSamplen(BigDecimal samplen) {
        this.samplen = samplen;
    }

    public List<CPKAnalyseChartDataVo> getCpkAnalyseChartDataVo() {
        return cpkAnalyseChartDataVo;
    }

    public void setCpkAnalyseChartDataVo(List<CPKAnalyseChartDataVo> cpkAnalyseChartDataVo) {
        this.cpkAnalyseChartDataVo = cpkAnalyseChartDataVo;
    }
}
