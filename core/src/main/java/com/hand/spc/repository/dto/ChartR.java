package com.hand.spc.repository.dto;

import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.system.dto.BaseDTO;

import io.swagger.annotations.ApiModel;

@ApiModel("控制图")
@Table(name = "pspc_chart")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChartR extends BaseDTO {

	 @Id
	    @GeneratedValue
	    private Long chartId;
	    private Long tenantId;
	    private Long siteId;
	    private String chartCode;
	    private String description;
	    private String chartType;
	    private String multiMapSingle;
	    private String multiMapNormality;
	    private String multiMapCp;
	    private String multiMapPlato;
	    private String chartTitle;
	    private String triggerMessage;
	    private Long subgroupSize;
	    private Long maxPlotPoints;
	    private String xTickLabel;
	    @Transient
	    private List<ChartDetailR> chartDetailList;

	    public Long getChartId() {
	        return chartId;
	    }

	    public void setChartId(Long chartId) {
	        this.chartId = chartId;
	    }

	    public Long getTenantId() {
	        return tenantId;
	    }

	    public void setTenantId(Long tenantId) {
	        this.tenantId = tenantId;
	    }

	    public Long getSiteId() {
	        return siteId;
	    }

	    public void setSiteId(Long siteId) {
	        this.siteId = siteId;
	    }

	    public String getChartCode() {
	        return chartCode;
	    }

	    public void setChartCode(String chartCode) {
	        this.chartCode = chartCode;
	    }

	    public String getDescription() {
	        return description;
	    }

	    public void setDescription(String description) {
	        this.description = description;
	    }

	    public String getChartType() {
	        return chartType;
	    }

	    public void setChartType(String chartType) {
	        this.chartType = chartType;
	    }

	    public String getMultiMapSingle() {
	        return multiMapSingle;
	    }

	    public void setMultiMapSingle(String multiMapSingle) {
	        this.multiMapSingle = multiMapSingle;
	    }

	    public String getMultiMapNormality() {
	        return multiMapNormality;
	    }

	    public void setMultiMapNormality(String multiMapNormality) {
	        this.multiMapNormality = multiMapNormality;
	    }

	    public String getMultiMapCp() {
	        return multiMapCp;
	    }

	    public void setMultiMapCp(String multiMapCp) {
	        this.multiMapCp = multiMapCp;
	    }

	    public String getMultiMapPlato() {
	        return multiMapPlato;
	    }

	    public void setMultiMapPlato(String multiMapPlato) {
	        this.multiMapPlato = multiMapPlato;
	    }

	    public String getChartTitle() {
	        return chartTitle;
	    }

	    public void setChartTitle(String chartTitle) {
	        this.chartTitle = chartTitle;
	    }

	    public String getTriggerMessage() {
	        return triggerMessage;
	    }

	    public void setTriggerMessage(String triggerMessage) {
	        this.triggerMessage = triggerMessage;
	    }

	    public Long getSubgroupSize() {
	        return subgroupSize;
	    }

	    public void setSubgroupSize(Long subgroupSize) {
	        this.subgroupSize = subgroupSize;
	    }

	    public Long getMaxPlotPoints() {
	        return maxPlotPoints;
	    }

	    public void setMaxPlotPoints(Long maxPlotPoints) {
	        this.maxPlotPoints = maxPlotPoints;
	    }

	    public String getxTickLabel() {
	        return xTickLabel;
	    }

	    public void setxTickLabel(String xTickLabel) {
	        this.xTickLabel = xTickLabel;
	    }

	    public List<ChartDetailR> getChartDetailList() {
	        return chartDetailList;
	    }

	    public void setChartDetailList(List<ChartDetailR> chartDetailList) {
	        this.chartDetailList = chartDetailList;
	    }
}
