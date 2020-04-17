package com.hand.spc.pspc_box_plot.view;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class BoxPlotVO implements Serializable{


    private static final long serialVersionUID = 6857189855219810278L;

    //实体控制图Code
    private String entityCode;

    //实体控制图 DESC
    private String description;

    //样本开始时间
    private String startDate;

    //样本结束时间
    private String endDate;

    //实体控制图 版本
    private String entityVersion;

    //实体控制图Id
    private Long entityId;

    //最大点
    private Long maxPlotPoints;

    //时间
    private Date sampleTime;

    //值
    private Long sampleValue;

    //下四分位数Q1
    private Double q1;

    //中位数Q2：
    private Double q2;

    //上四分位数Q3：
    private Double q3;

    //四分位距（IQR）
    private Double iqr;

    //内上限
    private Double iul;

    //内下限
    private Double ill;

    //外上限
    private Double eul;

    //	外下限
    private Double ell;

    //	上边缘（最大值）
    private Double umMax;

    //下边缘（最小值）
    private Double lmMin;

    //用●（红色）表示
    private List<Long> range1;

    //用"*" （红色）表示。
    private List<Long> range2;

    // 时间STR
    private String timeStr;

    //控制组描述
    private String ceParameterName;

    private List<BoxPlotVO> boxPlotVOSList;

    private List<?> list;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public List<BoxPlotVO> getBoxPlotVOSList() {
        return boxPlotVOSList;
    }

    public void setBoxPlotVOSList(List<BoxPlotVO> boxPlotVOSList) {
        this.boxPlotVOSList = boxPlotVOSList;
    }

    public Double getQ2() {
        return q2;
    }

    public void setQ2(Double q2) {
        this.q2 = q2;
    }

    public Double getQ3() {
        return q3;
    }

    public void setQ3(Double q3) {
        this.q3 = q3;
    }

    public Double getIqr() {
        return iqr;
    }

    public void setIqr(Double iqr) {
        this.iqr = iqr;
    }

    public Double getIul() {
        return iul;
    }

    public void setIul(Double iul) {
        this.iul = iul;
    }

    public Double getIll() {
        return ill;
    }

    public void setIll(Double ill) {
        this.ill = ill;
    }

    public Double getEul() {
        return eul;
    }

    public void setEul(Double eul) {
        this.eul = eul;
    }

    public Double getEll() {
        return ell;
    }

    public void setEll(Double ell) {
        this.ell = ell;
    }

    public Double getUmMax() {
        return umMax;
    }

    public void setUmMax(Double umMax) {
        this.umMax = umMax;
    }

    public Double getLmMin() {
        return lmMin;
    }

    public void setLmMin(Double lmMin) {
        this.lmMin = lmMin;
    }

    public List<Long> getRange1() {
        return range1;
    }

    public void setRange1(List<Long> range1) {
        this.range1 = range1;
    }

    public List<Long> getRange2() {
        return range2;
    }

    public void setRange2(List<Long> range2) {
        this.range2 = range2;
    }

    public Double getQ1() {
        return q1;
    }

    public void setQ1(Double q1) {
        this.q1 = q1;
    }

    public Date getSampleTime() {
        return sampleTime;
    }

    public void setSampleTime(Date sampleTime) {
        this.sampleTime = sampleTime;
    }

    public Long getSampleValue() {
        return sampleValue;
    }

    public void setSampleValue(Long sampleValue) {
        this.sampleValue = sampleValue;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    public Long getMaxPlotPoints() {
        return maxPlotPoints;
    }

    public void setMaxPlotPoints(Long maxPlotPoints) {
        this.maxPlotPoints = maxPlotPoints;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEntityVersion() {
        return entityVersion;
    }

    public void setEntityVersion(String entityVersion) {
        this.entityVersion = entityVersion;
    }

    public String getEntityCode() {
        return entityCode;
    }

    public void setEntityCode(String entityCode) {
        this.entityCode = entityCode;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCeParameterName() {
        return ceParameterName;
    }

    public void setCeParameterName(String ceParameterName) {
        this.ceParameterName = ceParameterName;
    }
}