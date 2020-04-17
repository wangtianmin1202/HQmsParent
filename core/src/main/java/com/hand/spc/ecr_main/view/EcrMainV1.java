package com.hand.spc.ecr_main.view;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hand.spc.ecr_main.dto.EcrMain;

public class EcrMainV1 extends EcrMain {
	private String solution;
	private String realName;
	
	private String itemFlag;
	private String sampleFlag;
	private String issueMsg; 
	
	// 创建时间从
	@JsonFormat(pattern="yyyy-MM-dd") 
	private Date startDate;  
	
	// 创建时间至
	@JsonFormat(pattern="yyyy-MM-dd") 
	private Date endDate;
	
	// 第一个关联词
	private String relevanceFirst;
	
	// 第二个关联词
	private String relevanceSecond;
	
	// 计划切换时间
	@JsonFormat(pattern="yyyy-MM-dd") 
	private Date planChangeDate; 
	
	// 实际切换时间
	@JsonFormat(pattern="yyyy-MM-dd") 
	private Date actChangeDate; 
	
	// Ecr 进度
	private String ecrTraking;
	
	// 风险程度多选值
	private List<String> riskdegreeList;
	
	public List<String> getRiskdegreeList() {
		return riskdegreeList;
	}
	public void setRiskdegreeList(List<String> riskdegreeList) {
		this.riskdegreeList = riskdegreeList;
	}
	public String getItemFlag() {
		return itemFlag;
	}
	public void setItemFlag(String itemFlag) {
		this.itemFlag = itemFlag;
	}
	public String getSampleFlag() {
		return sampleFlag;
	}
	public void setSampleFlag(String sampleFlag) {
		this.sampleFlag = sampleFlag;
	}
	public String getIssueMsg() {
		return issueMsg;	
	}
	public void setIssueMsg(String issueMsg) {
		this.issueMsg = issueMsg;
	}
	public String getSolution() {
		return solution;
	}
	public void setSolution(String solution) {
		this.solution = solution;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getRelevanceFirst() {
		return relevanceFirst;
	}
	public void setRelevanceFirst(String relevanceFirst) {
		this.relevanceFirst = relevanceFirst;
	}
	public String getRelevanceSecond() {
		return relevanceSecond;
	}
	public void setRelevanceSecond(String relevanceSecond) {
		this.relevanceSecond = relevanceSecond;
	}
	public Date getPlanChangeDate() {
		return planChangeDate;
	}
	public void setPlanChangeDate(Date planChangeDate) {
		this.planChangeDate = planChangeDate;
	}
	public Date getActChangeDate() {
		return actChangeDate;
	}
	public void setActChangeDate(Date actChangeDate) {
		this.actChangeDate = actChangeDate;
	}
	public String getEcrTraking() {
		return ecrTraking;
	}
	public void setEcrTraking(String ecrTraking) {
		this.ecrTraking = ecrTraking;
	}
	
	
}

