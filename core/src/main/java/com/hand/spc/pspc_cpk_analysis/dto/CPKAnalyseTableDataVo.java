package com.hand.spc.pspc_cpk_analysis.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class CPKAnalyseTableDataVo {

    private int SerialNumber=0;//序号
    private Date sampleTime;//样本时间

    List<BigDecimal> sampleValueList;


    private BigDecimal subgroupBar;//平均值
    private BigDecimal subgroupR;//极差
    private BigDecimal subgroupMax;//最大值
    private BigDecimal subgroupMin;//最小值
    private BigDecimal subgroupSigma;//标准差
    private BigDecimal subgroupMe;//中位数

    public List<BigDecimal> getSampleValueList() {
        return sampleValueList;
    }

    public void setSampleValueList(List<BigDecimal> sampleValueList) {
        this.sampleValueList = sampleValueList;
    }

    public int getSerialNumber() {
        return SerialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        SerialNumber = serialNumber;
    }

    public Date getSampleTime() {
        return sampleTime;
    }

    public void setSampleTime(Date sampleTime) {
        this.sampleTime = sampleTime;
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
}
