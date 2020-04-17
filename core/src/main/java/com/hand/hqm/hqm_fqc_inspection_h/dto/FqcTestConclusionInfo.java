package com.hand.hqm.hqm_fqc_inspection_h.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * FQC检验结论反馈WMS
 * 
 * @author kai.li
 * @version date:2020年2月26日16:29:59
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FqcTestConclusionInfo {

	private String plantCode;// 工厂编码

	private String orderNo;// 工单号

	private String orderCheckResult;// 工单检验结论

	private String serialNumber;// SN号

	private String snCheckResult;// SN检验结论

	private String inspectionNum;

	private String itemCode;

	private String lotNumber;

	private String itemVersion;

	private String spreading;

	private String okQty;

	private String ngQty;

	private String dealMethod;

	private String result;

	private String sn;

	public String getInspectionNum() {
		return inspectionNum;
	}

	public void setInspectionNum(String inspectionNum) {
		this.inspectionNum = inspectionNum;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getLotNumber() {
		return lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
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

	public String getOkQty() {
		return okQty;
	}

	public void setOkQty(String okQty) {
		this.okQty = okQty;
	}

	public String getNgQty() {
		return ngQty;
	}

	public void setNgQty(String ngQty) {
		this.ngQty = ngQty;
	}

	public String getDealMethod() {
		return dealMethod;
	}

	public void setDealMethod(String dealMethod) {
		this.dealMethod = dealMethod;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getPlantCode() {
		return plantCode;
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderCheckResult() {
		return orderCheckResult;
	}

	public void setOrderCheckResult(String orderCheckResult) {
		this.orderCheckResult = orderCheckResult;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getSnCheckResult() {
		return snCheckResult;
	}

	public void setSnCheckResult(String snCheckResult) {
		this.snCheckResult = snCheckResult;
	}

}
