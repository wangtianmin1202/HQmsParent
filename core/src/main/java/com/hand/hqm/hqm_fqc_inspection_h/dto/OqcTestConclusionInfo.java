package com.hand.hqm.hqm_fqc_inspection_h.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * OQC检验结论反馈WMS
 * @author kai.li
 * @version date:2020年2月26日17:37:39
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OqcTestConclusionInfo {

	private String plantCode;//工厂编码
	
	private String itemCode;//物料编码
	
	private String itemVersion;//物料版本
	
	private String spreading;//档位号
	
	private String lotNumber;//批次号
	
	private String inspectionResult;//检验结论

	public String getPlantCode() {
		return plantCode;
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
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

	public String getSpreading() {
		return spreading;
	}

	public void setSpreading(String spreading) {
		this.spreading = spreading;
	}

	public String getLotNumber() {
		return lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	public String getInspectionResult() {
		return inspectionResult;
	}

	public void setInspectionResult(String inspectionResult) {
		this.inspectionResult = inspectionResult;
	}

	
}
