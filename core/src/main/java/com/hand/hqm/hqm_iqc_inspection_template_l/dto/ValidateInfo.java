package com.hand.hqm.hqm_iqc_inspection_template_l.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * IQC检验模板数据导入-验证数据信息
 * @author kai.li
 * @version date:2020年2月25日10:10:09
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidateInfo {

	private String plant;
	
	private String itemCode;
	
	private String itemVersion;
	
	private String attributeName;
	
	private String attributeTool;
	
	private String sourceType;

	private String categoryCode;
	
	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getPlant() {
		return plant;
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemVersion() {
		return itemVersion;
	}

	public void setItemVersion(String itemVersion) {
		this.itemVersion = itemVersion;
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
