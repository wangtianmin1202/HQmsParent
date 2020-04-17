package com.hand.spc.ecr_main.view;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class EcrMaterialResultV0  implements Serializable{
	
	private List<String> itemCodes;
	private List<Float> qtys;
	private Long groupId;
	private String status;
	private Float scrap;
	private Long cycleDays;
	private Date adviceChangeTime;
	public List<String> getItemCodes() {
		return itemCodes;
	}
	public void setItemCodes(List<String> itemCodes) {
		this.itemCodes = itemCodes;
	}
	public List<Float> getQtys() {
		return qtys;
	}
	public void setQtys(List<Float> qtys) {
		this.qtys = qtys;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Float getScrap() {
		return scrap;
	}
	public void setScrap(Float scrap) {
		this.scrap = scrap;
	}
	public Long getCycleDays() {
		return cycleDays;
	}
	public void setCycleDays(Long cycleDays) {
		this.cycleDays = cycleDays;
	}
	public Date getAdviceChangeTime() {
		return adviceChangeTime;
	}
	public void setAdviceChangeTime(Date adviceChangeTime) {
		this.adviceChangeTime = adviceChangeTime;
	}	
	
	
	 
	
	
}
