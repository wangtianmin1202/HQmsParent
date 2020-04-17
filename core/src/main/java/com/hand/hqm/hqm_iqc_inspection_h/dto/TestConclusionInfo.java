package com.hand.hqm.hqm_iqc_inspection_h.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * IQC检验结论反馈WMS
 * @author kai.li
 * @version date:2020年2月26日15:01:47
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestConclusionInfo {

	private String plantCode;//工厂编码
	
	private String inspectionNum;//单号
	
	private Float qty;//订单行号
	
	private String itemCode;//物料编号
	
	private String itemVersion;//物料版本
	
	private String spreading;//档位号
	
	private String lotNumber;//批次号
	
	private String dealMethod;//检验结论
	
	private String itemStatus;//po订单号

	
	public String getInspectionNum() {
		return inspectionNum;
	}

	public void setInspectionNum(String inspectionNum) {
		this.inspectionNum = inspectionNum;
	}

	public Float getQty() {
		return qty;
	}

	public void setQty(Float qty) {
		this.qty = qty;
	}

	public String getDealMethod() {
		return dealMethod;
	}

	public void setDealMethod(String dealMethod) {
		this.dealMethod = dealMethod;
	}

	public String getItemStatus() {
		return itemStatus;
	}

	public void setItemStatus(String itemStatus) {
		this.itemStatus = itemStatus;
	}

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

	public String getLotNumber() {
		return lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

}
