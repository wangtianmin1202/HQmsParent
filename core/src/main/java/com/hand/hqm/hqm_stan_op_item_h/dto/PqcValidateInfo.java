package com.hand.hqm.hqm_stan_op_item_h.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * PQC检验模板数据导入-验证数据信息
 * @author kai.li
 * @version date:2020年2月25日10:10:09
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PqcValidateInfo {

	private String plantCode;//工厂
	
	private String prodLine;//产线
	
	private String standardOp;//工序
	
	private String workstation;//工位
	
	private String itemCode;//物料
	
	private String attributeName;//检验项目
	
	private String attributeTool;//检验要求

	public String getPlantCode() {
		return plantCode;
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}

	public String getProdLine() {
		return prodLine;
	}

	public void setProdLine(String prodLine) {
		this.prodLine = prodLine;
	}

	public String getStandardOp() {
		return standardOp;
	}

	public void setStandardOp(String standardOp) {
		this.standardOp = standardOp;
	}

	public String getWorkstation() {
		return workstation;
	}

	public void setWorkstation(String workstation) {
		this.workstation = workstation;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeTool() {
		return attributeTool;
	}

	public void setAttributeTool(String attributeTool) {
		this.attributeTool = attributeTool;
	}

	
}
