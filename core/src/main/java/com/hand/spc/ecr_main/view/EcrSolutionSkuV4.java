package com.hand.spc.ecr_main.view;

import java.io.Serializable;
import java.util.List;

public class EcrSolutionSkuV4 implements Serializable{
	private String ecrno;
	private List<String> itemIds;
	public String getEcrno() {
		return ecrno;
	}
	public void setEcrno(String ecrno) {
		this.ecrno = ecrno;
	}
	public List<String> getItemIds() {
		return itemIds;
	}
	public void setItemIds(List<String> itemIds) {
		this.itemIds = itemIds;
	}	
}
