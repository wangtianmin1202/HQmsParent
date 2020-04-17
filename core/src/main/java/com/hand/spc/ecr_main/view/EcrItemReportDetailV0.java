package com.hand.spc.ecr_main.view;

import java.io.Serializable;
import java.util.List;

public class EcrItemReportDetailV0 implements Serializable {
	private String skuCode;
	private String skuDesc;
	private String skuId;
	private String bomUse;
	private List<EcrItemReportDetailV1> ecrItemReportDetailV1s;
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public String getSkuDesc() {
		return skuDesc;
	}
	public void setSkuDesc(String skuDesc) {
		this.skuDesc = skuDesc;
	}
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public String getBomUse() {
		return bomUse;
	}
	public void setBomUse(String bomUse) {
		this.bomUse = bomUse;
	}
	public List<EcrItemReportDetailV1> getEcrItemReportDetailV1s() {
		return ecrItemReportDetailV1s;
	}
	public void setEcrItemReportDetailV1s(List<EcrItemReportDetailV1> ecrItemReportDetailV1s) {
		this.ecrItemReportDetailV1s = ecrItemReportDetailV1s;
	}
	
	
}
