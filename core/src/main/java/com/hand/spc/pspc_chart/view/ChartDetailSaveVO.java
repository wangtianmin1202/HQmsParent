package com.hand.spc.pspc_chart.view;

import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

public class ChartDetailSaveVO implements Serializable {

    private static final long serialVersionUID = -8081612161372568209L;

    /*
    头Id
     */
    private Float chartId;


    /*
    主要控制图数据
     */
    private Float chartDetailIdMain;

    private Float tenantIdMain;

    private Float siteIdMain;

    private String chartDetailTypeMain;

    private String xAxisLabelMain;

    private String yAxisLabelMain;

    private Float yAxisMaxMain;

    private Float yAxisMinMain;

    private String controlLimitUsageMain;

    private Float upperControlLimitMain;

    private Float centerLineMain;

    private Float lowerControlLimitMain;

    private String displaySpecLimitMain;

    private Float upperSpecLimitMain;

    private Float specTargetMain;

    private Float lowerSpecLimitMain;

    private String enableJudgeGroupMain;

    private Float judgementGroupIdMain;


    /*
    次要控制图数据
     */
    private Float chartDetailIdSecond;

    private Float tenantIdSecond;

    private Float siteIdSecond;

    private String chartDetailTypeSecond;

    private String xAxisLabelSecond;

    private String yAxisLabelSecond;

    private Float yAxisMaxSecond;

    private Float yAxisMinSecond;

    private String controlLimitUsageSecond;

    private Float upperControlLimitSecond;

    private Float centerLineSecond;

    private Float lowerControlLimitSecond;

    private String displaySpecLimitSecond;

    private Float upperSpecLimitSecond;

    private Float specTargetSecond;

    private Float lowerSpecLimitSecond;

    private String enableJudgeGroupSecond;

    private Float judgementGroupIdSecond;


    public Float getChartId() {
        return chartId;
    }

    public void setChartId(Float chartId) {
        this.chartId = chartId;
    }

    public Float getChartDetailIdMain() {
        return chartDetailIdMain;
    }

    public void setChartDetailIdMain(Float chartDetailIdMain) {
        this.chartDetailIdMain = chartDetailIdMain;
    }

    public Float getTenantIdMain() {
        return tenantIdMain;
    }

    public void setTenantIdMain(Float tenantIdMain) {
        this.tenantIdMain = tenantIdMain;
    }

    public Float getSiteIdMain() {
        return siteIdMain;
    }

    public void setSiteIdMain(Float siteIdMain) {
        this.siteIdMain = siteIdMain;
    }

    public String getChartDetailTypeMain() {
        return chartDetailTypeMain;
    }

    public void setChartDetailTypeMain(String chartDetailTypeMain) {
        this.chartDetailTypeMain = chartDetailTypeMain;
    }

    public String getxAxisLabelMain() {
        return xAxisLabelMain;
    }

    public void setxAxisLabelMain(String xAxisLabelMain) {
        this.xAxisLabelMain = xAxisLabelMain;
    }

    public String getyAxisLabelMain() {
        return yAxisLabelMain;
    }

    public void setyAxisLabelMain(String yAxisLabelMain) {
        this.yAxisLabelMain = yAxisLabelMain;
    }

    public Float getyAxisMaxMain() {
        return yAxisMaxMain;
    }

    public void setyAxisMaxMain(Float yAxisMaxMain) {
        this.yAxisMaxMain = yAxisMaxMain;
    }

    public Float getyAxisMinMain() {
        return yAxisMinMain;
    }

    public void setyAxisMinMain(Float yAxisMinMain) {
        this.yAxisMinMain = yAxisMinMain;
    }

    public String getControlLimitUsageMain() {
        return controlLimitUsageMain;
    }

    public void setControlLimitUsageMain(String controlLimitUsageMain) {
        this.controlLimitUsageMain = controlLimitUsageMain;
    }

    public Float getUpperControlLimitMain() {
        return upperControlLimitMain;
    }

    public void setUpperControlLimitMain(Float upperControlLimitMain) {
        this.upperControlLimitMain = upperControlLimitMain;
    }

    public Float getCenterLineMain() {
        return centerLineMain;
    }

    public void setCenterLineMain(Float centerLineMain) {
        this.centerLineMain = centerLineMain;
    }

    public Float getLowerControlLimitMain() {
        return lowerControlLimitMain;
    }

    public void setLowerControlLimitMain(Float lowerControlLimitMain) {
        this.lowerControlLimitMain = lowerControlLimitMain;
    }

    public String getDisplaySpecLimitMain() {
        return displaySpecLimitMain;
    }

    public void setDisplaySpecLimitMain(String displaySpecLimitMain) {
        this.displaySpecLimitMain = displaySpecLimitMain;
    }

    public Float getUpperSpecLimitMain() {
        return upperSpecLimitMain;
    }

    public void setUpperSpecLimitMain(Float upperSpecLimitMain) {
        this.upperSpecLimitMain = upperSpecLimitMain;
    }

    public Float getSpecTargetMain() {
        return specTargetMain;
    }

    public void setSpecTargetMain(Float specTargetMain) {
        this.specTargetMain = specTargetMain;
    }

    public Float getLowerSpecLimitMain() {
        return lowerSpecLimitMain;
    }

    public void setLowerSpecLimitMain(Float lowerSpecLimitMain) {
        this.lowerSpecLimitMain = lowerSpecLimitMain;
    }

    public String getEnableJudgeGroupMain() {
        return enableJudgeGroupMain;
    }

    public void setEnableJudgeGroupMain(String enableJudgeGroupMain) {
        this.enableJudgeGroupMain = enableJudgeGroupMain;
    }

    public Float getJudgementGroupIdMain() {
        return judgementGroupIdMain;
    }

    public void setJudgementGroupIdMain(Float judgementGroupIdMain) {
        this.judgementGroupIdMain = judgementGroupIdMain;
    }

    public Float getChartDetailIdSecond() {
        return chartDetailIdSecond;
    }

    public void setChartDetailIdSecond(Float chartDetailIdSecond) {
        this.chartDetailIdSecond = chartDetailIdSecond;
    }

    public Float getTenantIdSecond() {
        return tenantIdSecond;
    }

    public void setTenantIdSecond(Float tenantIdSecond) {
        this.tenantIdSecond = tenantIdSecond;
    }

    public Float getSiteIdSecond() {
        return siteIdSecond;
    }

    public void setSiteIdSecond(Float siteIdSecond) {
        this.siteIdSecond = siteIdSecond;
    }

    public String getChartDetailTypeSecond() {
        return chartDetailTypeSecond;
    }

    public void setChartDetailTypeSecond(String chartDetailTypeSecond) {
        this.chartDetailTypeSecond = chartDetailTypeSecond;
    }

    public String getxAxisLabelSecond() {
        return xAxisLabelSecond;
    }

    public void setxAxisLabelSecond(String xAxisLabelSecond) {
        this.xAxisLabelSecond = xAxisLabelSecond;
    }

    public String getyAxisLabelSecond() {
        return yAxisLabelSecond;
    }

    public void setyAxisLabelSecond(String yAxisLabelSecond) {
        this.yAxisLabelSecond = yAxisLabelSecond;
    }

    public Float getyAxisMaxSecond() {
        return yAxisMaxSecond;
    }

    public void setyAxisMaxSecond(Float yAxisMaxSecond) {
        this.yAxisMaxSecond = yAxisMaxSecond;
    }

    public Float getyAxisMinSecond() {
        return yAxisMinSecond;
    }

    public void setyAxisMinSecond(Float yAxisMinSecond) {
        this.yAxisMinSecond = yAxisMinSecond;
    }

    public String getControlLimitUsageSecond() {
        return controlLimitUsageSecond;
    }

    public void setControlLimitUsageSecond(String controlLimitUsageSecond) {
        this.controlLimitUsageSecond = controlLimitUsageSecond;
    }

    public Float getUpperControlLimitSecond() {
        return upperControlLimitSecond;
    }

    public void setUpperControlLimitSecond(Float upperControlLimitSecond) {
        this.upperControlLimitSecond = upperControlLimitSecond;
    }

    public Float getCenterLineSecond() {
        return centerLineSecond;
    }

    public void setCenterLineSecond(Float centerLineSecond) {
        this.centerLineSecond = centerLineSecond;
    }

    public Float getLowerControlLimitSecond() {
        return lowerControlLimitSecond;
    }

    public void setLowerControlLimitSecond(Float lowerControlLimitSecond) {
        this.lowerControlLimitSecond = lowerControlLimitSecond;
    }

    public String getDisplaySpecLimitSecond() {
        return displaySpecLimitSecond;
    }

    public void setDisplaySpecLimitSecond(String displaySpecLimitSecond) {
        this.displaySpecLimitSecond = displaySpecLimitSecond;
    }

    public Float getUpperSpecLimitSecond() {
        return upperSpecLimitSecond;
    }

    public void setUpperSpecLimitSecond(Float upperSpecLimitSecond) {
        this.upperSpecLimitSecond = upperSpecLimitSecond;
    }

    public Float getSpecTargetSecond() {
        return specTargetSecond;
    }

    public void setSpecTargetSecond(Float specTargetSecond) {
        this.specTargetSecond = specTargetSecond;
    }

    public Float getLowerSpecLimitSecond() {
        return lowerSpecLimitSecond;
    }

    public void setLowerSpecLimitSecond(Float lowerSpecLimitSecond) {
        this.lowerSpecLimitSecond = lowerSpecLimitSecond;
    }

    public String getEnableJudgeGroupSecond() {
        return enableJudgeGroupSecond;
    }

    public void setEnableJudgeGroupSecond(String enableJudgeGroupSecond) {
        this.enableJudgeGroupSecond = enableJudgeGroupSecond;
    }

    public Float getJudgementGroupIdSecond() {
        return judgementGroupIdSecond;
    }

    public void setJudgementGroupIdSecond(Float judgementGroupIdSecond) {
        this.judgementGroupIdSecond = judgementGroupIdSecond;
    }
}
