package com.hand.hap.webservice.ws.idto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * WMS库存重验信息
 * @author kai.li
 * @version date:2020年2月25日09:07:51
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemStock {

	private String plantCode;//工厂编码
	
	private String itemCode;//物料编号
	
	private String itemVersion;//物料版本
	
	private String spreading;//档位号
	
	private String warehouseCode;//仓库
	
	private String lotNumber;//批次号
	
	private Float qty;//库存数量
	
	private String warningDate;//预警日期

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

	public String getItemVersion() {
		return itemVersion;
	}

	public void setItemVersion(String itemVersion) {
		this.itemVersion = itemVersion;
	}

	public String getSpreading() {
		return spreading;
	}

	public void setSpreading(String spreading) {
		this.spreading = spreading;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getLotNumber() {
		return lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	public Float getQty() {
		return qty;
	}

	public void setQty(Float qty) {
		this.qty = qty;
	}

	public String getWarningDate() {
		return warningDate;
	}

	public void setWarningDate(String warningDate) {
		this.warningDate = warningDate;
	}
	
}
