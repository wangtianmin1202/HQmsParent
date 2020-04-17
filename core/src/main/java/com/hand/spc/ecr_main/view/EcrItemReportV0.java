package com.hand.spc.ecr_main.view;

import java.io.Serializable;
import java.util.List;

import com.hand.spc.ecr_main.view  .EcrItemReportV1;

/**
 * @author KOCE3C3
 *	库存头查询对象
 */
public class EcrItemReportV0 implements Serializable{

	private String itemId;
	private String itemCode;
	private String itemDesc;
	private String buyer;
	private List<EcrItemReportV1> EcrItemReportV1s;
	
	
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	public String getBuyer() {
		return buyer;
	}
	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}
	public List<EcrItemReportV1> getEcrItemReportV1s() {
		return EcrItemReportV1s;
	}
	public void setEcrItemReportV1s(List<EcrItemReportV1> EcrItemReportV1s) {
		this.EcrItemReportV1s = EcrItemReportV1s;
	}
	
	
}
