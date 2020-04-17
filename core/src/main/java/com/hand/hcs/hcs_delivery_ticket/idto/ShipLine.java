package com.hand.hcs.hcs_delivery_ticket.idto;

import java.util.List;

/**
 * @author tainmin.wang
 * @version date：2019年11月6日 上午9:38:55
 * 
 */
public class ShipLine {
	private String ticketLineNum;

	private String itemCode;

	private String itemVersion;

//	private String supplierCode;

	private String packingSize;

	private String packetInfo;

	private String productionBatch;

	private String spreading;

	private String shipQuantity;

	private String uomCode;

	private String remarks;

	private String poNumber;

	private String poLineNum;

	private String attribute1;

	private String attribute2;

	private String attribute3;

	private String attribute4;

	private String attribute5;

	private List<BarCode> barCodes;

	public String getPoLineNum() {
		return poLineNum;
	}

	public void setPoLineNum(String poLineNum) {
		this.poLineNum = poLineNum;
	}

	public String getTicketLineNum() {
		return ticketLineNum;
	}

	public void setTicketLineNum(String ticketLineNum) {
		this.ticketLineNum = ticketLineNum;
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

//	public String getSupplierCode() {
//		return supplierCode;
//	}
//
//	public void setSupplierCode(String supplierCode) {
//		this.supplierCode = supplierCode;
//	}

	public String getPackingSize() {
		return packingSize;
	}

	public void setPackingSize(String packingSize) {
		this.packingSize = packingSize;
	}

	public String getPacketInfo() {
		return packetInfo;
	}

	public void setPacketInfo(String packetInfo) {
		this.packetInfo = packetInfo;
	}

	public String getProductionBatch() {
		return productionBatch;
	}

	public void setProductionBatch(String productionBatch) {
		this.productionBatch = productionBatch;
	}

	public String getSpreading() {
		return spreading;
	}

	public void setSpreading(String spreading) {
		this.spreading = spreading;
	}

	public String getShipQuantity() {
		return shipQuantity;
	}

	public void setShipQuantity(String shipQuantity) {
		this.shipQuantity = shipQuantity;
	}

	public String getUomCode() {
		return uomCode;
	}

	public void setUomCode(String uomCode) {
		this.uomCode = uomCode;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getAttribute1() {
		return attribute1;
	}

	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
	}

	public String getAttribute2() {
		return attribute2;
	}

	public void setAttribute2(String attribute2) {
		this.attribute2 = attribute2;
	}

	public String getAttribute3() {
		return attribute3;
	}

	public void setAttribute3(String attribute3) {
		this.attribute3 = attribute3;
	}

	public String getAttribute4() {
		return attribute4;
	}

	public void setAttribute4(String attribute4) {
		this.attribute4 = attribute4;
	}

	public String getAttribute5() {
		return attribute5;
	}

	public void setAttribute5(String attribute5) {
		this.attribute5 = attribute5;
	}

	public List<BarCode> getBarCodes() {
		return barCodes;
	}

	public void setBarCodes(List<BarCode> barCodes) {
		this.barCodes = barCodes;
	}

}
