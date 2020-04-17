package com.hand.spc.ecr_main.view;

import java.io.Serializable;

public class EcrItemReportV2 implements Serializable{
	private Float poQty;			
	private Float supplierOnhand;
	private Float supplierWipOnhand;
	private Float calculateOnhand;
	private Float specialOnhand;
	public Float getPoQty() {
		return poQty;
	}
	public void setPoQty(Float poQty) {
		this.poQty = poQty;
	}
	public Float getSupplierOnhand() {
		return supplierOnhand;
	}
	public void setSupplierOnhand(Float supplierOnhand) {
		this.supplierOnhand = supplierOnhand;
	}
	public Float getSupplierWipOnhand() {
		return supplierWipOnhand;
	}
	public void setSupplierWipOnhand(Float supplierWipOnhand) {
		this.supplierWipOnhand = supplierWipOnhand;
	}
	public Float getCalculateOnhand() {
		return calculateOnhand;
	}
	public void setCalculateOnhand(Float calculateOnhand) {
		this.calculateOnhand = calculateOnhand;
	}
	public Float getSpecialOnhand() {
		return specialOnhand;
	}
	public void setSpecialOnhand(Float specialOnhand) {
		this.specialOnhand = specialOnhand;
	}
	
	
	
}
