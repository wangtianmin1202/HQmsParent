package com.hand.spc.ecr_main.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class EcrItemReportV3 implements Serializable{
	private BigDecimal demandQty;
	
	private Date  demandDate;

	public BigDecimal getDemandQty() {
		return demandQty;
	}

	public void setDemandQty(BigDecimal demandQty) {
		this.demandQty = demandQty;
	}

	public Date getDemandDate() {
		return demandDate;
	}

	public void setDemandDate(Date demandDate) {
		this.demandDate = demandDate;
	}
	
}
