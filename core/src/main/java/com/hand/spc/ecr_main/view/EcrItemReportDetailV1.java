package com.hand.spc.ecr_main.view;

import java.io.Serializable;

public class EcrItemReportDetailV1 implements Serializable{	
	public String getCompleteQty() {
		return completeQty;
	}
	public void setCompleteQty(String completeQty) {
		this.completeQty = completeQty;
	}
	public String getDemandQty() {
		return demandQty;
	}
	public void setDemandQty(String demandQty) {
		this.demandQty = demandQty;
	}
	public String getLeftQty() {
		return leftQty;
	}
	public void setLeftQty(String leftQty) {
		this.leftQty = leftQty;
	}
	
	
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}


	private String completeQty;
	private String demandQty;
	private String leftQty;
	private String month;
}
