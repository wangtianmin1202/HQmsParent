package com.hand.hcs.hcs_delivery_ticket.dto;

public class TicketReport {
	/*
	 * 供应商编码
	 */
	private String supplierCode;
	/*
	 * 供应商名称
	 */
	private String supplierName;
	/*
	 * 第一列
	 */
	private Long column1;
	/*
	 * 第二列
	 */
	private Long column2;
	/*
	 * 第三列
	 */
	private Long column3;
	/*
	 * 第四列
	 */
	private Long column4;
	/*
	 * 第五列
	 */
	private Long column5;
	/*
	 * 合格总数
	 */
	private Long qualifiedCount;
	/**
	 * 总数
	 */
	private Long count;
	/**
	 * 分数
	 */
	private Float molecular;
	
	public String getSupplierCode() {
		return supplierCode;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public Long getQualifiedCount() {
		return qualifiedCount;
	}
	public Long getCount() {
		return count;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public void setQualifiedCount(Long qualifiedCount) {
		this.qualifiedCount = qualifiedCount;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public Long getColumn1() {
		return column1;
	}
	public Long getColumn2() {
		return column2;
	}
	public Long getColumn3() {
		return column3;
	}
	public Long getColumn4() {
		return column4;
	}
	public Long getColumn5() {
		return column5;
	}
	public void setColumn1(Long column1) {
		this.column1 = column1;
	}
	public void setColumn2(Long column2) {
		this.column2 = column2;
	}
	public void setColumn3(Long column3) {
		this.column3 = column3;
	}
	public void setColumn4(Long column4) {
		this.column4 = column4;
	}
	public void setColumn5(Long column5) {
		this.column5 = column5;
	}
	public Float getMolecular() {
		return molecular;
	}
	public void setMolecular(Float molecular) {
		this.molecular = molecular;
	}
	
	
}
