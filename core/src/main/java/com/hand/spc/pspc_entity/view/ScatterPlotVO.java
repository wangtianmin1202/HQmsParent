package com.hand.spc.pspc_entity.view;

import com.hand.spc.pspc_entity.dto.Entity;
import com.hand.utils.date.DateUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScatterPlotVO extends Entity {

    private static final long serialVersionUID = 7438832335691308250L;

    private String individualAnalysis;//是否单个分析 Y:是 N:否

    private String coordinateAxis;//单个分析时的坐标 abscissa:横坐标 ordinate:纵坐标

    private Date startDate;//开始时间

    private Date endDate;//结束时间

    private String startDateStr;//开始时间

    private String endDateStr;//结束时间

    private Long maxPlotPoints;//最大绘制点数

    private List<Double> sampleValues;//点位集合

    private List<Double[]> scatterPlotPoints;//散点图点位集合

    private Map<String, String> scatterPlotSchema;//散点图XY轴名 key: X/Y value:坐标轴名称

    private List<List<Object>> matrixScatterPlotPoints;//矩阵散点图点位集合

    private List<Map<String, String>> matrixScatterPlotSchema;//矩阵散点图矩阵块集合 key:name/text value:名称/显示文字

    private int pointCount;//图表点位总数

    private List<List<Object>> tableData;//表格数据

    private List<ScatterPlotVO> inVo;//原始VO信息 带样本数据值


    public String getIndividualAnalysis() {
        return individualAnalysis;
    }

    public void setIndividualAnalysis(String individualAnalysis) {
        this.individualAnalysis = individualAnalysis;
    }

    public String getCoordinateAxis() {
        return coordinateAxis;
    }

    public void setCoordinateAxis(String coordinateAxis) {
        this.coordinateAxis = coordinateAxis;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
        this.startDateStr = DateUtil.dateToString(startDate);
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
        this.endDateStr = DateUtil.dateToString(endDate);
    }

    public Long getMaxPlotPoints() {
        return maxPlotPoints;
    }

    public void setMaxPlotPoints(Long maxPlotPoints) {
        this.maxPlotPoints = maxPlotPoints;
    }

    public String getStartDateStr() {
        return startDateStr;
    }

    public void setStartDateStr(String startDateStr) {
        this.startDateStr = startDateStr;
    }

    public String getEndDateStr() {
        return endDateStr;
    }

    public void setEndDateStr(String endDateStr) {
        this.endDateStr = endDateStr;
    }

//    public List<Long> getSampleValues() {
//        return sampleValues;
//    }
//
//    public void setSampleValues(List<Long> sampleValues) {
//        this.sampleValues = sampleValues;
//    }


    public List<Double> getSampleValues() {
        return sampleValues;
    }

    public void setSampleValues(List<Double> sampleValues) {
        this.sampleValues = sampleValues;
    }

//    public List<Long[]> getScatterPlotPoints() {
//        return scatterPlotPoints;
//    }
//
//    public void setScatterPlotPoints(List<Long[]> scatterPlotPoints) {
//        this.scatterPlotPoints = scatterPlotPoints;
//    }


    public List<Double[]> getScatterPlotPoints() {
        return scatterPlotPoints;
    }

    public void setScatterPlotPoints(List<Double[]> scatterPlotPoints) {
        this.scatterPlotPoints = scatterPlotPoints;
    }

    public List<List<Object>> getMatrixScatterPlotPoints() {
        return matrixScatterPlotPoints;
    }

    public void setMatrixScatterPlotPoints(List<List<Object>> matrixScatterPlotPoints) {
        this.matrixScatterPlotPoints = matrixScatterPlotPoints;
    }

    public List<Map<String, String>> getMatrixScatterPlotSchema() {
        return matrixScatterPlotSchema;
    }

    public void setMatrixScatterPlotSchema(List<Map<String, String>> matrixScatterPlotSchema) {
        this.matrixScatterPlotSchema = matrixScatterPlotSchema;
    }

    public int getPointCount() {
        return pointCount;
    }

    public void setPointCount(int pointCount) {
        this.pointCount = pointCount;
    }

    public List<ScatterPlotVO> getInVo() {
        return inVo;
    }

    public void setInVo(List<ScatterPlotVO> inVo) {
        this.inVo = inVo;
    }

    public Map<String, String> getScatterPlotSchema() {
        return scatterPlotSchema;
    }

    public void setScatterPlotSchema(Map<String, String> scatterPlotSchema) {
        this.scatterPlotSchema = scatterPlotSchema;
    }

    public List<List<Object>> getTableData() {
        return tableData;
    }

    public void setTableData(List<List<Object>> tableData) {
        this.tableData = tableData;
    }
}