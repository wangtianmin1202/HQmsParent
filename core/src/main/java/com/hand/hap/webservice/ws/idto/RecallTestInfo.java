package com.hand.hap.webservice.ws.idto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * WMS返修品/成品召回信息
 * @author kai.li
 * @version date:2020年2月25日10:10:09
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecallTestInfo {

	private String plantCode;//工厂编码
	
	private String sourceCode;//来源单据号
	
	private String itemCode;//物料编号
	
	private String itemVersion;//物料版本
	
	private String lotNumber;//批次号
	
	private Float receiveQty;//接收数量
	
	private String evenTime;//业务发生时间

	public String getPlantCode() {
		return plantCode;
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
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

	public String getEvenTime() {
		return evenTime;
	}

	public void setEvenTime(String evenTime) {
		this.evenTime = evenTime;
	}

}
