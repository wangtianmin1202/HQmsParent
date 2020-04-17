package com.hand.spc.utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Halen Chang
 */
public class RegressionEquationVo implements Serializable {

    private static final long serialVersionUID = 6420523096039133499L;

    private int count;

    private BigDecimal lopeB;

    private BigDecimal alpha;

    private BigDecimal rValue;

    private BigDecimal muXBD;

    private BigDecimal muYBD;

    private List<Long> xScotList;

    private List<List> yScotList;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<Long> getxScotList() {
        return xScotList;
    }

    public void setxScotList(List<Long> xScotList) {
        this.xScotList = xScotList;
    }

    public List<List> getyScotList() {
        return yScotList;
    }

    public void setyScotList(List<List> yScotList) {
        this.yScotList = yScotList;
    }

    public RegressionEquationVo(int count, BigDecimal lopeB, BigDecimal alpha, BigDecimal rValue, BigDecimal muXBD, BigDecimal muYBD){
        this.count = count;
        this.lopeB = lopeB;
        this.alpha = alpha;
        this.rValue = rValue;
        this.muXBD = muXBD;
        this.muYBD = muYBD;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public BigDecimal getLopeB() {
        return lopeB;
    }

    public void setLopeB(BigDecimal lopeB) {
        this.lopeB = lopeB;
    }

    public BigDecimal getAlpha() {
        return alpha;
    }

    public void setAlpha(BigDecimal alpha) {
        this.alpha = alpha;
    }

    public BigDecimal getrValue() {
        return rValue;
    }

    public void setrValue(BigDecimal rValue) {
        this.rValue = rValue;
    }

    public BigDecimal getMuXBD() {
        return muXBD;
    }

    public void setMuXBD(BigDecimal muXBD) {
        this.muXBD = muXBD;
    }

    public BigDecimal getMuYBD() {
        return muYBD;
    }

    public void setMuYBD(BigDecimal muYBD) {
        this.muYBD = muYBD;
    }
}
