package com.hand.hqm.hqm_item_control.dto;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * OQC库存超期基础数据
 * @author kai.li
 * @version date:2020年2月26日10:28:33
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockOverdueInfo {

	private String plantCode;//工厂编码
	
	private String itemCode;//物料编号
	
	private Float warningDate;//预警周期

	public String getPlantCode() {
		return plantCode;
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public Float getWarningDate() {
		return warningDate;
	}

	public void setWarningDate(Float warningDate) {
		this.warningDate = warningDate;
	}

	
}
