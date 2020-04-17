package com.hand.spc.ecr_main.view;

import java.io.Serializable;
import java.util.Date;

public class EcrItemReportV1 implements Serializable{
	private Long reportId;
	private Float sumQty;
	private Float onhandQty;
	private Float buyerQty;
	private Date  planDate;
	
	
	
	public Long getReportId() {
		return reportId;
	}
	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}
	public Float getSumQty() {
		return sumQty;
	}
	public void setSumQty(Float sumQty) {
		this.sumQty = sumQty;
	}
	public Float getOnhandQty() {
		return onhandQty;
	}
	public void setOnhandQty(Float onhandQty) {
		this.onhandQty = onhandQty;
	}
	public Float getBuyerQty() {
		return buyerQty;
	}
	public void setBuyerQty(Float buyerQty) {
		this.buyerQty = buyerQty;
	}
	public Date getPlanDate() {
		return planDate;
	}
	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}
		
}
