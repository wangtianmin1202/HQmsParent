package com.hand.spc.ecr_main.view;

import java.io.Serializable;

public class EcrSolutionSkuV3 implements Serializable {
	private String skuId;
	
	private String itemId;

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	
	
}
