package com.hand.spc.ecr_main.view;

import java.io.Serializable;
import java.util.List;

public class EcrSolutionSkuV0 implements Serializable {
	private String skuId;
	private String skuDescription;
	private String sumYears;
	private String sumItemCost;
	private String sumWorkCost;
	private String sumCost;
	private String itemVersion;
	
	private String issueType;
	private String issueMsg;
	private String ecrno;
	private String itemSkuId;
	
	
	
	
	
	public String getItemSkuId() {
		return itemSkuId;
	}
	public void setItemSkuId(String itemSkuId) {
		this.itemSkuId = itemSkuId;
	}
	public String getEcrno() {
		return ecrno;
	}
	public void setEcrno(String ecrno) {
		this.ecrno = ecrno;
	}
	public String getIssueType() {
		return issueType;
	}
	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}
	public String getIssueMsg() {
		return issueMsg;
	}
	public void setIssueMsg(String issueMsg) {
		this.issueMsg = issueMsg;
	}
	public String getItemVersion() {
		return itemVersion;
	}
	public void setItemVersion(String itemVersion) {
		this.itemVersion = itemVersion;
	}
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public String getSkuDescription() {
		return skuDescription;
	}
	public void setSkuDescription(String skuDescription) {
		this.skuDescription = skuDescription;
	}
	public String getSumYears() {
		return sumYears;
	}
	public void setSumYears(String sumYears) {
		this.sumYears = sumYears;
	}
	public String getSumItemCost() {
		return sumItemCost;
	}
	public void setSumItemCost(String sumItemCost) {
		this.sumItemCost = sumItemCost;
	}
	public String getSumWorkCost() {
		return sumWorkCost;
	}
	public void setSumWorkCost(String sumWorkCost) {
		this.sumWorkCost = sumWorkCost;
	}
	public String getSumCost() {
		return sumCost;
	}
	public void setSumCost(String sumCost) {
		this.sumCost = sumCost;
	}
}
