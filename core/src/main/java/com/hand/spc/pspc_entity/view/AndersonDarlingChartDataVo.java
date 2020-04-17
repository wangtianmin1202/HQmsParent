package com.hand.spc.pspc_entity.view;

import java.math.BigDecimal;

/**
 * description
 *
 * @author 60201 2019/08/26 18:54
 */
public class AndersonDarlingChartDataVo {

    private BigDecimal xAbscissa;//	横坐标
    private BigDecimal yOrdinates;//纵坐标
    private BigDecimal zOrdinates;

    public AndersonDarlingChartDataVo() {
    }

    public AndersonDarlingChartDataVo(BigDecimal xAbscissa, BigDecimal yOrdinates) {
        this.xAbscissa = xAbscissa;
        this.yOrdinates = yOrdinates;
    }

    public AndersonDarlingChartDataVo(BigDecimal xAbscissa, BigDecimal yOrdinates, BigDecimal zOrdinates) {
        this.xAbscissa = xAbscissa;
        this.yOrdinates = yOrdinates;
        this.zOrdinates = zOrdinates;
    }

    public BigDecimal getxAbscissa() {
        return xAbscissa;
    }

    public void setxAbscissa(BigDecimal xAbscissa) {
        this.xAbscissa = xAbscissa;
    }

    public BigDecimal getyOrdinates() {
        return yOrdinates;
    }

    public void setyOrdinates(BigDecimal yOrdinates) {
        this.yOrdinates = yOrdinates;
    }

    public BigDecimal getzOrdinates() {
        return zOrdinates;
    }

    public void setzOrdinates(BigDecimal zOrdinates) {
        this.zOrdinates = zOrdinates;
    }
}
