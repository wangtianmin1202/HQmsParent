package com.hand.spc.ecr_main.view;

import java.io.Serializable;
import java.util.List;

public class EcrMainV0 implements Serializable {
	private String ecrno;
	private String car;
	private String createBy;
	private String creationDate;
	private String issue;
	private String issueType;
	private String issueDate;
	private String issueFrom;
	private String risk;
	private String ufr;
	private String finishDate;
	private List<String> materialIds;
	private List<String> sampleIds;
	private String ecrType;
	private String riskMeaning;
	
	
	public String getEcrType() {
		return ecrType;
	}
	public void setEcrType(String ecrType) {
		this.ecrType = ecrType;
	}
	public String getRiskMeaning() {
		return riskMeaning;
	}
	public void setRiskMeaning(String riskMeaning) {
		this.riskMeaning = riskMeaning;
	}
	public String getEcrno() {
		return ecrno;
	}
	public void setEcrno(String ecrno) {
		this.ecrno = ecrno;
	}
	public String getCar() {
		return car;
	}
	public void setCar(String car) {
		this.car = car;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public String getIssueType() {
		return issueType;
	}
	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}
	public String getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}
	public String getIssueFrom() {
		return issueFrom;
	}
	public void setIssueFrom(String issueFrom) {
		this.issueFrom = issueFrom;
	}
	public String getRisk() {
		return risk;
	}
	public void setRisk(String risk) {
		this.risk = risk;
	}
	public String getUfr() {
		return ufr;
	}
	public void setUfr(String ufr) {
		this.ufr = ufr;
	}
	public String getFinishDate() {
		return finishDate;
	}
	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}
	public List<String> getMaterialIds() {
		return materialIds;
	}
	public void setMaterialIds(List<String> materialIds) {
		this.materialIds = materialIds;
	}
	public List<String> getSampleIds() {
		return sampleIds;
	}
	public void setSampleIds(List<String> sampleIds) {
		this.sampleIds = sampleIds;
	}
	
}
