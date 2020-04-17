package com.hand.spc.ecr_main.view;

import java.io.Serializable;

public class EcrCreateTmpV0 implements Serializable {
	private String skuCode;
	
	private String itemSkuId;

	private String itemSkuSolutionId;
	
	private Float bomUse;
	
	
	
	public Float getBomUse() {
		return bomUse;
	}

	public void setBomUse(Float bomUse) {
		this.bomUse = bomUse;
	}

	public String getItemSkuSolutionId() {
		return itemSkuSolutionId;
	}

	public void setItemSkuSolutionId(String itemSkuSolutionId) {
		this.itemSkuSolutionId = itemSkuSolutionId;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getItemSkuId() {
		return itemSkuId;
	}

	public void setItemSkuId(String itemSkuId) {
		this.itemSkuId = itemSkuId;
	}
	
	
}
