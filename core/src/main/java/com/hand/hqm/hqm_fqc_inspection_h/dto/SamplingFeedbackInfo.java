package com.hand.hqm.hqm_fqc_inspection_h.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * FQC加严抽样数反馈
 * @author kai.li
 * @version date:2020年2月26日09:41:17
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SamplingFeedbackInfo {
	
	private String plantCode;//工厂编码
	
	private String sourceOrder;//工单单号
	
	private String itemCode;//物料编号
	
	private String itemVersion;//物料版本
	
	private Float receiveQty;//抽样数量
	
	private String inspectionNum;
	
	private String spreading;
	
	private String lotNumber;
	
	private String inspectionPlace;

	public String getInspectionNum() {
		return inspectionNum;
	}

	public void setInspectionNum(String inspectionNum) {
		this.inspectionNum = inspectionNum;
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

	public String getInspectionPlace() {
		return inspectionPlace;
	}

	public void setInspectionPlace(String inspectionPlace) {
		this.inspectionPlace = inspectionPlace;
	}

	public String getPlantCode() {
		return plantCode;
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}

	public String getSourceOrder() {
		return sourceOrder;
	}

	public void setSourceOrder(String sourceOrder) {
		this.sourceOrder = sourceOrder;
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

	public Float getReceiveQty() {
		return receiveQty;
	}

	public void setReceiveQty(Float receiveQty) {
		this.receiveQty = receiveQty;
	}
	
	
}
