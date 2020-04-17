package com.hand.spc.pspc_entity.view;

import com.hand.spc.pspc_entity.dto.Entity;
import com.hand.utils.date.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ParametricComparisonVO extends Entity {

    private static final long serialVersionUID = -6313775228183884796L;

    private Date startDate;//开始时间

    private Date endDate;//结束时间

    private String startDateStr;//开始时间

    private String endDateStr;//结束时间

    private Long maxPlotPoints;//最大绘制点数

    private List<Double> sampleValues;//点位集合

    private List<List<Object>> tableData;//表格数据

    List<List<Double>> parametricComparisonPoints = new ArrayList<>();//折线图点位集合

    List<Map<String, String>> parametricComparisonSchema = new ArrayList<>();//折线图schema集合 key:name/text value:名称/显示文字

    private List<ParametricComparisonVO> inVo;//原始VO信息 带样本数据值


    public Date getStartDate() {
        if (startDate != null) {
            return (Date) startDate.clone();
        } else {
            return null;
        }
    }

    public void setStartDate(Date startDate) {
        if (startDate == null) {
            this.startDate = null;
        } else {
            this.startDate = (Date) startDate.clone();
            this.startDateStr = DateUtil.dateToString(startDate);
        }
    }

    public Date getEndDate() {
        if (endDate != null) {
            return (Date) endDate.clone();
        } else {
            return null;
        }
    }

    public void setEndDate(Date endDate) {
        if (endDate == null) {
            this.endDate = null;
        } else {
            this.endDate = (Date) endDate.clone();
            this.endDateStr = DateUtil.dateToString(endDate);
        }
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

    public Long getMaxPlotPoints() {
        return maxPlotPoints;
    }

    public void setMaxPlotPoints(Long maxPlotPoints) {
        this.maxPlotPoints = maxPlotPoints;
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

    public List<List<Object>> getTableData() {
        return tableData;
    }

    public void setTableData(List<List<Object>> tableData) {
        this.tableData = tableData;
    }

//    public List<List<Long>> getParametricComparisonPoints() {
//        return parametricComparisonPoints;
//    }
//
//    public void setParametricComparisonPoints(List<List<Long>> parametricComparisonPoints) {
//        this.parametricComparisonPoints = parametricComparisonPoints;
//    }


    public List<List<Double>> getParametricComparisonPoints() {
        return parametricComparisonPoints;
    }

    public void setParametricComparisonPoints(List<List<Double>> parametricComparisonPoints) {
        this.parametricComparisonPoints = parametricComparisonPoints;
    }

    public List<Map<String, String>> getParametricComparisonSchema() {
        return parametricComparisonSchema;
    }

    public void setParametricComparisonSchema(List<Map<String, String>> parametricComparisonSchema) {
        this.parametricComparisonSchema = parametricComparisonSchema;
    }

    public List<ParametricComparisonVO> getInVo() {
        return inVo;
    }

    public void setInVo(List<ParametricComparisonVO> inVo) {
        this.inVo = inVo;
    }
}