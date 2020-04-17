package com.hand.spc.ecr_main.view;

import java.io.Serializable;
import java.util.Date;

public class EcrApsTmpV0 implements Serializable {
	private Float qty;
	private Date month;
	
	
	public Float getQty() {
		return qty;
	}
	public void setQty(Float qty) {
		this.qty = qty;
	}
	public Date getMonth() {
		return month;
	}
	public void setMonth(Date month) {
		this.month = month;
	}
	
	
}
