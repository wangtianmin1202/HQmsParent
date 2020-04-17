package com.hand.spc.ecr_main.view;

import java.io.Serializable;
import java.util.Date;

public class EcrApsV0 implements Serializable{
	
	private Long itemId;
	private Float qty;
	private String demandDate;
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
	public String getDemandDate() {
		return demandDate;
	}
	public void setDemandDate(String demandDate) {
		this.demandDate = demandDate;
	}
 
	
	

}
