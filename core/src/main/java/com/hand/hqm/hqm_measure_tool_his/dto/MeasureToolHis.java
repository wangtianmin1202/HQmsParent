package com.hand.hqm.hqm_measure_tool_his.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hand.hap.system.dto.BaseDTO;
import java.util.Date;
import java.util.List;

@ExtensionAttribute(disable = true)
@Table(name = "HQM_MEASURE_TOOL_HIS")
public class MeasureToolHis extends BaseDTO {

	public static final String FIELD_MEASURE_TOOL_HIS_ID = "measureToolHisId";
	public static final String FIELD_MEASURE_TOOL_ID = "measureToolId";
	public static final String FIELD_MEASURE_TOOL_NUM = "measureToolNum";
	public static final String FIELD_DESCRIPTIONS = "descriptions";
	public static final String FIELD_MEASURE_TOOL_TYPE = "measureToolType";
	public static final String FIELD_MEASURE_TOOL_POSITION_STATUS = "measureToolPositionStatus";
	public static final String FIELD_MEASURE_TOOL_STATUS = "measureToolStatus";
	public static final String FIELD_MEASURE_TOOL_SUPPLIER = "measureToolSupplier";
	public static final String FIELD_MEASUREMENT_ACCURACY = "measurementAccuracy";
	public static final String FIELD_PURCHASE_DATE = "purchaseDate";
	public static final String FIELD_CHECK_TYPE = "checkType";
	public static final String FIELD_CHECK_CYCLE = "checkCycle";
	public static final String FIELD_LAST_CHECK_DATE = "lastCheckDate";
	public static final String FIELD_NEXT_CHECK_DATE = "nextCheckDate";
	public static final String FIELD_CHECKED_BY = "checkedBy";
	public static final String FIELD_USE_DEPARTMENT = "useDepartment";
	public static final String FIELD_PRINCIPAL = "principal";
	public static final String FIELD_MSA_FLAG = "msaFlag";
	public static final String FIELD_ENABLE_FLAG = "enableFlag";
	public static final String FIELD_REMARK = "remark";
	public static final String FIELD_PLANT_ID = "plantId";
	public static final String FIELD_MEASURING_RANGE = "measuringRange";
	public static final String FIELD_FIRST_USE_DATE = "firstUseDate";
	public static final String FIELD_OUTBOUND_DATE = "outboundDate";
	public static final String FIELD_MSA_TYPE = "msaType";
	public static final String FIELD_JUDGEMENT_STANDARD = "judgementStandard";
	public static final String FIELD_MSA_STATUS = "msaStatus";
	public static final String FIELD_KEY_PROCESS = "keyProcess";
	public static final String FIELD_POSITION_TITLE = "positionTitle";
	public static final String FIELD_MSA_RESULT = "msaResult";
	public static final String FIELD_MSA_CYCLE = "msaCycle";
	public static final String FIELD_CHECK_RESULT = "checkResult";

	@Id
	@GeneratedValue
	private Float measureToolHisId;

	private Float measureToolId;

	@Length(max = 50)
	private String measureToolNum;

	@Length(max = 50)
	private String descriptions;

	@Length(max = 5)
	private String measureToolType;

	@Length(max = 5)
	private String measureToolPositionStatus;

	@Length(max = 5)
	private String measureToolStatus;

	private Float measureToolSupplier;
	@Length(max = 50)
	private String measurementAccuracy;

	private Date purchaseDate;

	@Length(max = 5)
	private String checkType;

	@Length(max = 5)
	private String checkCycle;

	private Date lastCheckDate;

	private Date nextCheckDate;

	@Length(max = 50)
	private String checkedBy;

	private Float useDepartment;

	private Float principal;

	@Length(max = 5)
	private String msaFlag;

	@Length(max = 5)
	private String enableFlag;

	@Length(max = 300)
	private String remark;

	private Float plantId;

	@Length(max = 50)
	private String measuringRange;

	private Date firstUseDate;

	private Date outboundDate;

	@Length(max = 5)
	private String msaType;

	@Length(max = 5)
	private String judgementStandard;

	@Length(max = 5)
	private String msaStatus;

	@Length(max = 5)
	private String keyProcess;

	@Length(max = 50)
	private String positionTitle;

	@Length(max = 5)
	private String msaResult;

	@Length(max = 5)
	private String msaCycle;

	@Length(max = 5)
	private String checkResult;
	
	@Length(max = 50)
	private String measureToolMaker;

	@Length(max = 20)
	private String uom;

	private Date returnDate;
	
	@Length(max = 20)
	private String hisType;
	@Length(max = 300)
	private String scrapReason;

	@Length(max = 300)
	private String repairReason;
	
	private Date lastUpdateDate;
	
	private Float laseUpdatedBy;
	
	@Transient
	private String measureToolName;// 量具名称

	@Transient
	private String startTime;// 下次校验日期从
	@Transient
	private String endTime;// 下次校验日期至
	@Transient
	private String msaContent;// MSA分析内容
	@Transient
	private List<String> msaContentList;// MSA分析内容
	@Transient
	private String userName;// 管理人
	@Transient
	private String name;// 使用部门
	@Transient
	private Float userId;
	@Transient
	private Float unitId;
	@Transient
	private String meaning;
	@Transient
	private Float analystId;
	@Transient
	private String updateFlag;
	@Transient
	private String supplierName;
	@Transient
	private String measureToolSupplierName;
	@Transient
	private String plantName;
	@Transient
	private String plantCode;
	@Transient
	private String updatedName;
	@Transient
	private String checkDateStart;
	@Transient
	private String checkDateEnd;
	
	public String getCheckDateStart() {
		return checkDateStart;
	}

	public void setCheckDateStart(String checkDateStart) {
		this.checkDateStart = checkDateStart;
	}

	public String getCheckDateEnd() {
		return checkDateEnd;
	}

	public void setCheckDateEnd(String checkDateEnd) {
		this.checkDateEnd = checkDateEnd;
	}

	public String getUpdatedName() {
		return updatedName;
	}

	public void setUpdatedName(String updatedName) {
		this.updatedName = updatedName;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public Float getLaseUpdatedBy() {
		return laseUpdatedBy;
	}

	public void setLaseUpdatedBy(Float laseUpdatedBy) {
		this.laseUpdatedBy = laseUpdatedBy;
	}

	public String getScrapReason() {
		return scrapReason;
	}

	public void setScrapReason(String scrapReason) {
		this.scrapReason = scrapReason;
	}

	public String getRepairReason() {
		return repairReason;
	}

	public void setRepairReason(String repairReason) {
		this.repairReason = repairReason;
	}

	public String getMeasureToolMaker() {
		return measureToolMaker;
	}

	public void setMeasureToolMaker(String measureToolMaker) {
		this.measureToolMaker = measureToolMaker;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public String getHisType() {
		return hisType;
	}

	public void setHisType(String hisType) {
		this.hisType = hisType;
	}

	public String getMeaning() {
		return meaning;
	}

	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}

	public Float getAnalystId() {
		return analystId;
	}

	public void setAnalystId(Float analystId) {
		this.analystId = analystId;
	}

	public String getUpdateFlag() {
		return updateFlag;
	}

	public void setUpdateFlag(String updateFlag) {
		this.updateFlag = updateFlag;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getMeasureToolSupplierName() {
		return measureToolSupplierName;
	}

	public void setMeasureToolSupplierName(String measureToolSupplierName) {
		this.measureToolSupplierName = measureToolSupplierName;
	}

	public String getPlantName() {
		return plantName;
	}

	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}

	public String getPlantCode() {
		return plantCode;
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}

	public String getMeasureToolName() {
		return measureToolName;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public String getMsaContent() {
		return msaContent;
	}

	public List<String> getMsaContentList() {
		return msaContentList;
	}

	public String getUserName() {
		return userName;
	}

	public String getName() {
		return name;
	}

	public Float getUserId() {
		return userId;
	}

	public Float getUnitId() {
		return unitId;
	}

	public void setMeasureToolName(String measureToolName) {
		this.measureToolName = measureToolName;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setMsaContent(String msaContent) {
		this.msaContent = msaContent;
	}

	public void setMsaContentList(List<String> msaContentList) {
		this.msaContentList = msaContentList;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUserId(Float userId) {
		this.userId = userId;
	}

	public void setUnitId(Float unitId) {
		this.unitId = unitId;
	}

	public void setMeasureToolHisId(Float measureToolHisId) {
		this.measureToolHisId = measureToolHisId;
	}

	public Float getMeasureToolHisId() {
		return measureToolHisId;
	}

	public void setMeasureToolId(Float measureToolId) {
		this.measureToolId = measureToolId;
	}

	public Float getMeasureToolId() {
		return measureToolId;
	}

	public void setMeasureToolNum(String measureToolNum) {
		this.measureToolNum = measureToolNum;
	}

	public String getMeasureToolNum() {
		return measureToolNum;
	}

	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}

	public String getDescriptions() {
		return descriptions;
	}

	public void setMeasureToolType(String measureToolType) {
		this.measureToolType = measureToolType;
	}

	public String getMeasureToolType() {
		return measureToolType;
	}

	public void setMeasureToolPositionStatus(String measureToolPositionStatus) {
		this.measureToolPositionStatus = measureToolPositionStatus;
	}

	public String getMeasureToolPositionStatus() {
		return measureToolPositionStatus;
	}

	public void setMeasureToolStatus(String measureToolStatus) {
		this.measureToolStatus = measureToolStatus;
	}

	public String getMeasureToolStatus() {
		return measureToolStatus;
	}

	public void setMeasureToolSupplier(Float measureToolSupplier) {
		this.measureToolSupplier = measureToolSupplier;
	}

	public Float getMeasureToolSupplier() {
		return measureToolSupplier;
	}

	

	public String getMeasurementAccuracy() {
		return measurementAccuracy;
	}

	public void setMeasurementAccuracy(String measurementAccuracy) {
		this.measurementAccuracy = measurementAccuracy;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public String getCheckType() {
		return checkType;
	}

	public void setCheckCycle(String checkCycle) {
		this.checkCycle = checkCycle;
	}

	public String getCheckCycle() {
		return checkCycle;
	}

	public void setLastCheckDate(Date lastCheckDate) {
		this.lastCheckDate = lastCheckDate;
	}

	public Date getLastCheckDate() {
		return lastCheckDate;
	}

	public void setNextCheckDate(Date nextCheckDate) {
		this.nextCheckDate = nextCheckDate;
	}

	public Date getNextCheckDate() {
		return nextCheckDate;
	}

	public void setCheckedBy(String checkedBy) {
		this.checkedBy = checkedBy;
	}

	public String getCheckedBy() {
		return checkedBy;
	}

	public void setUseDepartment(Float useDepartment) {
		this.useDepartment = useDepartment;
	}

	public Float getUseDepartment() {
		return useDepartment;
	}

	public void setPrincipal(Float principal) {
		this.principal = principal;
	}

	public Float getPrincipal() {
		return principal;
	}

	public void setMsaFlag(String msaFlag) {
		this.msaFlag = msaFlag;
	}

	public String getMsaFlag() {
		return msaFlag;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}

	public String getEnableFlag() {
		return enableFlag;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemark() {
		return remark;
	}

	public void setPlantId(Float plantId) {
		this.plantId = plantId;
	}

	public Float getPlantId() {
		return plantId;
	}

	public void setMeasuringRange(String measuringRange) {
		this.measuringRange = measuringRange;
	}

	public String getMeasuringRange() {
		return measuringRange;
	}

	public void setFirstUseDate(Date firstUseDate) {
		this.firstUseDate = firstUseDate;
	}

	public Date getFirstUseDate() {
		return firstUseDate;
	}

	public void setOutboundDate(Date outboundDate) {
		this.outboundDate = outboundDate;
	}

	public Date getOutboundDate() {
		return outboundDate;
	}

	public void setMsaType(String msaType) {
		this.msaType = msaType;
	}

	public String getMsaType() {
		return msaType;
	}

	public void setJudgementStandard(String judgementStandard) {
		this.judgementStandard = judgementStandard;
	}

	public String getJudgementStandard() {
		return judgementStandard;
	}

	public void setMsaStatus(String msaStatus) {
		this.msaStatus = msaStatus;
	}

	public String getMsaStatus() {
		return msaStatus;
	}

	public void setKeyProcess(String keyProcess) {
		this.keyProcess = keyProcess;
	}

	public String getKeyProcess() {
		return keyProcess;
	}

	public void setPositionTitle(String positionTitle) {
		this.positionTitle = positionTitle;
	}

	public String getPositionTitle() {
		return positionTitle;
	}

	public void setMsaResult(String msaResult) {
		this.msaResult = msaResult;
	}

	public String getMsaResult() {
		return msaResult;
	}

	public void setMsaCycle(String msaCycle) {
		this.msaCycle = msaCycle;
	}

	public String getMsaCycle() {
		return msaCycle;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	public String getCheckResult() {
		return checkResult;
	}

}
