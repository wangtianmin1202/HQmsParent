package com.hand.hqm.hqm_iqc_inspection_h.dto;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * IQC抽样数信息
 * 
 * @author kai.li
 * @version date:2020年2月25日15:04:03
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SamplingNumInfo {

	private String plantCode;// 工厂编码

	private String sourceOrder;// 收货单号
	
	private String inspectionNum;// 检验单号
	
	private Float lineNum;// 订单行号

	private String itemCode;// 物料编号

	private String itemVersion;// 物料版本

	private String spreading;// 档位号

	private String lotNumber;// 批次号

	private Float receiveQty;// 抽样数量

	private String samplePackQty;
	
	private String isUrgency;// 是否加急
	
	private String inspectionPlace;// 物料检验地点
	
	private String evenTime;// 业务发生时间
	
	private String exemptFlag;

	public String getInspectionNum() {
		return inspectionNum;
	}

	public void setInspectionNum(String inspectionNum) {
		this.inspectionNum = inspectionNum;
	}

	public String getSamplePackQty() {
		return samplePackQty;
	}

	public void setSamplePackQty(String samplePackQty) {
		this.samplePackQty = samplePackQty;
	}

	public String getExemptFlag() {
		return exemptFlag;
	}

	public void setExemptFlag(String exemptFlag) {
		this.exemptFlag = exemptFlag;
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

	public Float getLineNum() {
		return lineNum;
	}

	public void setLineNum(Float lineNum) {
		this.lineNum = lineNum;
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

	public Float getReceiveQty() {
		return receiveQty;
	}

	public void setReceiveQty(Float receiveQty) {
		this.receiveQty = receiveQty;
	}

	public String getIsUrgency() {
		return isUrgency;
	}

	public void setIsUrgency(String isUrgency) {
		this.isUrgency = isUrgency;
	}

	public String getInspectionPlace() {
		return inspectionPlace;
	}

	public void setInspectionPlace(String inspectionPlace) {
		this.inspectionPlace = inspectionPlace;
	}

	public String getEvenTime() {
		return evenTime;
	}

	public void setEvenTime(String evenTime) {
		this.evenTime = evenTime;
	}

}
