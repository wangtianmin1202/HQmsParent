package com.hand.hqm.hqm_qc_task.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hand.hap.system.dto.BaseDTO;
import java.util.Date;

@ExtensionAttribute(disable = true)
@Table(name = "HQM_FQC_TASK")
public class FqcTask extends BaseDTO {

	@Id
	@GeneratedValue
	private Float taskId;

	private Float plantId;

	@Length(max = 50)
	private String sourceOrder;

	private Float prodLineId;

	@Length(max = 50)
	private String originalOrder;

	@Length(max = 30)
	private String sourceType;

	private Float itemId;

	@Length(max = 50)
	private String itemVersion;

	@Length(max = 50)
	private String spreading;

	@Length(max = 100)
	private String productionBatch;

	@Length(max = 30)
	private String inspectionType;

	private Float productQty;

	private Float sampleQty;

	private Date productDate;

	@Length(max = 30)
	private String taskFrom;

	@Length(max = 30)
	private String isUrgency;

	@Length(max = 30)
	private String solveStatus;

	@Length(max = 100)
	private String errorMsg;

	@Length(max = 30)
	private String inspectionNum;

	@Length(max = 30)
	private String inspectionRes;

	@Length(max = 50)
	private String dealMethod;

	@Transient
	private String itemCode;
	@Transient
	private String itemDescriptions;
	@Transient
	private String plantCode;
	@Transient
	private String productDateFrom;
	@Transient
	private String productDateTo;

	private String relOrder;

	private String recheckSampleWay;

	private String vtpNumber;
	
	private String orderType;
	
	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getVtpNumber() {
		return vtpNumber;
	}

	public void setVtpNumber(String vtpNumber) {
		this.vtpNumber = vtpNumber;
	}

	public String getRelOrder() {
		return relOrder;
	}

	public void setRelOrder(String relOrder) {
		this.relOrder = relOrder;
	}

	public String getRecheckSampleWay() {
		return recheckSampleWay;
	}

	public void setRecheckSampleWay(String recheckSampleWay) {
		this.recheckSampleWay = recheckSampleWay;
	}

	public String getProductDateFrom() {
		return productDateFrom;
	}

	public void setProductDateFrom(String productDateFrom) {
		this.productDateFrom = productDateFrom;
	}

	public String getProductDateTo() {
		return productDateTo;
	}

	public void setProductDateTo(String productDateTo) {
		this.productDateTo = productDateTo;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemDescriptions() {
		return itemDescriptions;
	}

	public void setItemDescriptions(String itemDescriptions) {
		this.itemDescriptions = itemDescriptions;
	}

	public String getPlantCode() {
		return plantCode;
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}

	public void setTaskId(Float taskId) {
		this.taskId = taskId;
	}

	public Float getTaskId() {
		return taskId;
	}

	public void setPlantId(Float plantId) {
		this.plantId = plantId;
	}

	public Float getPlantId() {
		return plantId;
	}

	public void setSourceOrder(String sourceOrder) {
		this.sourceOrder = sourceOrder;
	}

	public String getSourceOrder() {
		return sourceOrder;
	}

	public void setProdLineId(Float prodLineId) {
		this.prodLineId = prodLineId;
	}

	public Float getProdLineId() {
		return prodLineId;
	}

	public void setOriginalOrder(String originalOrder) {
		this.originalOrder = originalOrder;
	}

	public String getOriginalOrder() {
		return originalOrder;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setItemId(Float itemId) {
		this.itemId = itemId;
	}

	public Float getItemId() {
		return itemId;
	}

	public void setItemVersion(String itemVersion) {
		this.itemVersion = itemVersion;
	}

	public String getItemVersion() {
		return itemVersion;
	}

	public void setSpreading(String spreading) {
		this.spreading = spreading;
	}

	public String getSpreading() {
		return spreading;
	}

	public void setProductionBatch(String productionBatch) {
		this.productionBatch = productionBatch;
	}

	public String getProductionBatch() {
		return productionBatch;
	}

	public void setInspectionType(String inspectionType) {
		this.inspectionType = inspectionType;
	}

	public String getInspectionType() {
		return inspectionType;
	}

	public void setProductQty(Float productQty) {
		this.productQty = productQty;
	}

	public Float getProductQty() {
		return productQty;
	}

	public void setSampleQty(Float sampleQty) {
		this.sampleQty = sampleQty;
	}

	public Float getSampleQty() {
		return sampleQty;
	}

	public void setProductDate(Date productDate) {
		this.productDate = productDate;
	}

	public Date getProductDate() {
		return productDate;
	}

	public void setTaskFrom(String taskFrom) {
		this.taskFrom = taskFrom;
	}

	public String getTaskFrom() {
		return taskFrom;
	}

	public void setIsUrgency(String isUrgency) {
		this.isUrgency = isUrgency;
	}

	public String getIsUrgency() {
		return isUrgency;
	}

	public void setSolveStatus(String solveStatus) {
		this.solveStatus = solveStatus;
	}

	public String getSolveStatus() {
		return solveStatus;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setInspectionNum(String inspectionNum) {
		this.inspectionNum = inspectionNum;
	}

	public String getInspectionNum() {
		return inspectionNum;
	}

	public void setInspectionRes(String inspectionRes) {
		this.inspectionRes = inspectionRes;
	}

	public String getInspectionRes() {
		return inspectionRes;
	}

	public void setDealMethod(String dealMethod) {
		this.dealMethod = dealMethod;
	}

	public String getDealMethod() {
		return dealMethod;
	}

}
