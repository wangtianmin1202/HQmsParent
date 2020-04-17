package com.hand.hcs.hcs_delivery_ticket.idto;

import java.util.List;

/**
 * @author tainmin.wang
 * @version date：2019年11月6日 上午9:38:55
 * 
 */
public class ShipHeader {

	private String uuid;
	
	private String synDate;
	
	private String ticketNumber;
	
	private String supplierCode;
	
	private String attribute1;
	
	private String attribute2;
	
	private String attribute3;
	
	private String attribute4;
	
	private String attribute5;
	
	private List<ShipLine> shipLines;
	
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getSynDate() {
		return synDate;
	}
	public void setSynDate(String synDate) {
		this.synDate = synDate;
	}
	public String getTicketNumber() {
		return ticketNumber;
	}
	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
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
	public List<ShipLine> getShipLines() {
		return shipLines;
	}
	public void setShipLines(List<ShipLine> shipLines) {
		this.shipLines = shipLines;
	}

}
