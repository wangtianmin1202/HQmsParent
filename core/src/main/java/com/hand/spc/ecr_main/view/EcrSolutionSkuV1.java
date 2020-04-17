package com.hand.spc.ecr_main.view;

import java.io.Serializable;

public class EcrSolutionSkuV1 implements Serializable{
	private String itemCode;
	private String itemDesc;
	private String price;
	private String bomUse;
	private String ecrUse;
	private String uom;
	private String itemChangeType;
	private String subItemFlag;
	private String subItem;
	private String itemSkuKid;
	private String itemId;
	
	private String relationId;
	
	private String skuCode;
	
	
	
	
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public String getRelationId() {
		return relationId;
	}
	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemSkuKid() {
		return itemSkuKid;
	}
	public void setItemSkuKid(String itemSkuKid) {
		this.itemSkuKid = itemSkuKid;
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
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getBomUse() {
		return bomUse;
	}
	public void setBomUse(String bomUse) {
		this.bomUse = bomUse;
	}
	public String getEcrUse() {
		return ecrUse;
	}
	public void setEcrUse(String ecrUse) {
		this.ecrUse = ecrUse;
	}
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	public String getItemChangeType() {
		return itemChangeType;
	}
	public void setItemChangeType(String itemChangeType) {
		this.itemChangeType = itemChangeType;
	}
	public String getSubItemFlag() {
		return subItemFlag;
	}
	public void setSubItemFlag(String subItemFlag) {
		this.subItemFlag = subItemFlag;
	}
	public String getSubItem() {
		return subItem;
	}
	public void setSubItem(String subItem) {
		this.subItem = subItem;
	}
}
