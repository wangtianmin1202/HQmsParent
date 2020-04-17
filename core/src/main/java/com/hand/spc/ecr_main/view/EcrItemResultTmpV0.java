package com.hand.spc.ecr_main.view;

import java.io.Serializable;

public class EcrItemResultTmpV0 implements Serializable{
	private Long itemId;
	private String month;
	private Float qty;
	private String waste;
	 
	
	 
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public Float getQty() {
		return qty;
	}
	public void setQty(Float qty) {
		this.qty = qty;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	 
	public String getWaste() {
		return waste;
	}
	public void setWaste(String waste) {
		this.waste = waste;
	}
	
		
}
