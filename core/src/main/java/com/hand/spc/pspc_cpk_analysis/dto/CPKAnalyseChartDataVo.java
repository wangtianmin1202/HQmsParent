package com.hand.spc.pspc_cpk_analysis.dto;

import java.math.BigDecimal;

public class CPKAnalyseChartDataVo {

    private BigDecimal xCoordinate	;//	横坐标
    private BigDecimal 	yStripGraph	;//	纵坐标1（条形图）
    private String 	yDottedGraph;//	纵坐标2虚曲线图
    private String 	ySolidGraph	;//	纵坐标2实曲线图

    public BigDecimal getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(BigDecimal xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public BigDecimal getyStripGraph() {
        return yStripGraph;
    }

    public void setyStripGraph(BigDecimal yStripGraph) {
        this.yStripGraph = yStripGraph;
    }

    public String getyDottedGraph() {
        return yDottedGraph;
    }

    public void setyDottedGraph(String yDottedGraph) {
        this.yDottedGraph = yDottedGraph;
    }

    public String getySolidGraph() {
        return ySolidGraph;
    }

    public void setySolidGraph(String ySolidGraph) {
        this.ySolidGraph = ySolidGraph;
    }
}
