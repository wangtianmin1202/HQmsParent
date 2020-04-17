package com.hand.hap.webservice.ws.idto;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * WMS来料信息
 * @author kai.li
 * @version date:2020年2月24日14:05:05
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskInComeInspect {

	private String plantCode;//工厂编码
	
	private String sourceOrder;//收货单号
	
	private Float lineNumber;//订单行号
	
	private String itemCode;//物料编号
	
	private String itemVersion;//物料版本
	
	private String spreading;//档位号
	
	private String lotNumber;//批次号
	
	private Float receiveQty;//接受数量
	
	private String receiveMan;//接受人
	
	private String isUrgenc;//是否加急
	
	private String supplierCode;//供应商代码
	
	private Float packQty;//包装数
	
	private String packetInfo;//包装方式
	
	private String poNumber;//po订单号
	
	private String eventTime;//业务发生时间
	
	private String warehouseType;//厂库类型

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

	public Float getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(Float lineNumber) {
		this.lineNumber = lineNumber;
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

	public String getReceiveMan() {
		return receiveMan;
	}

	public void setReceiveMan(String receiveMan) {
		this.receiveMan = receiveMan;
	}

	public String getIsUrgenc() {
		return isUrgenc;
	}

	public void setIsUrgenc(String isUrgenc) {
		this.isUrgenc = isUrgenc;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public Float getPackQty() {
		return packQty;
	}

	public void setPackQty(Float packQty) {
		this.packQty = packQty;
	}

	public String getPacketInfo() {
		return packetInfo;
	}

	public void setPacketInfo(String packetInfo) {
		this.packetInfo = packetInfo;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getEventTime() {
		return eventTime;
	}

	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}

	public String getWarehouseType() {
		return warehouseType;
	}

	public void setWarehouseType(String warehouseType) {
		this.warehouseType = warehouseType;
	}
	
	
}
