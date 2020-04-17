package com.hand.hqm.hqm_iqc_inspection_template_h.dto;

import java.util.Date;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Table;
import javax.persistence.Transient;

import com.hand.hap.system.dto.BaseDTO;

@ExtensionAttribute(disable = true)
@Table(name = "HQM_IQC_INSPECTION_TEMPLATE_H")
public class IqcInspectionTemplateH extends BaseDTO {

	@Id
	@GeneratedValue
	private Float headerId;

	private Float timeLimit;

	private Float plantId;

	private Float itemId;

	@Length(max = 30)

	private String itemCategory;

	private String mainType;

	@Length(max = 30)
	@NotEmpty
	private String versionNum;

	@Length(max = 30)
	private String sourceType;

	@Length(max = 30)
	private String drawingNumber;

	@Length(max = 1)
	@NotEmpty
	private String enableFlag;

	private Float historyNum;

	@Length(max = 30)
	private String status;

	private String itemEdition;

	private Float sampleWayId;

	private String ecrNumber;

	@Transient
	private String itemCode;

	@Transient
	private String itemDescriptions;

	@Transient
	private String plantCode;

	@Transient
	private Date columnCreationDate;

	@Transient
	private String itemCategoryCode;

	@Transient
	private String sampleWayCode;

	@Transient
	private String sampleWayDescription;

	@Transient
	private String sampleProcedureType;

	@Transient
	private String inspectionLevels;

	@Transient
	private String acceptanceQualityLimit;

	private String vtpNumebr;
	
	public String getVtpNumebr() {
		return vtpNumebr;
	}

	public void setVtpNumebr(String vtpNumebr) {
		this.vtpNumebr = vtpNumebr;
	}

	public String getEcrNumber() {
		return ecrNumber;
	}

	public void setEcrNumber(String ecrNumber) {
		this.ecrNumber = ecrNumber;
	}

	public String getSampleProcedureType() {
		return sampleProcedureType;
	}

	public void setSampleProcedureType(String sampleProcedureType) {
		this.sampleProcedureType = sampleProcedureType;
	}

	public String getInspectionLevels() {
		return inspectionLevels;
	}

	public void setInspectionLevels(String inspectionLevels) {
		this.inspectionLevels = inspectionLevels;
	}

	public String getAcceptanceQualityLimit() {
		return acceptanceQualityLimit;
	}

	public void setAcceptanceQualityLimit(String acceptanceQualityLimit) {
		this.acceptanceQualityLimit = acceptanceQualityLimit;
	}

	public String getSampleWayDescription() {
		return sampleWayDescription;
	}

	public void setSampleWayDescription(String sampleWayDescription) {
		this.sampleWayDescription = sampleWayDescription;
	}

	public String getSampleWayCode() {
		return sampleWayCode;
	}

	public void setSampleWayCode(String sampleWayCode) {
		this.sampleWayCode = sampleWayCode;
	}

	public String getItemEdition() {
		return itemEdition;
	}

	public void setItemEdition(String itemEdition) {
		this.itemEdition = itemEdition;
	}

	public Float getSampleWayId() {
		return sampleWayId;
	}

	public void setSampleWayId(Float sampleWayId) {
		this.sampleWayId = sampleWayId;
	}

	public String getMainType() {
		return mainType;
	}

	public void setMainType(String mainType) {
		this.mainType = mainType;
	}

	public Float getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(Float timeLimit) {
		this.timeLimit = timeLimit;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getItemCategoryCode() {
		return itemCategoryCode;
	}

	public void setItemCategoryCode(String itemCategoryCode) {
		this.itemCategoryCode = itemCategoryCode;
	}

	public Date getColumnCreationDate() {
		return columnCreationDate;
	}

	public void setColumnCreationDate(Date columnCreationDate) {
		this.columnCreationDate = columnCreationDate;
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

	public void setHeaderId(Float headerId) {
		this.headerId = headerId;
	}

	public Float getHeaderId() {
		return headerId;
	}

	public void setPlantId(Float plantId) {
		this.plantId = plantId;
	}

	public Float getPlantId() {
		return plantId;
	}

	public void setItemId(Float itemId) {
		this.itemId = itemId;
	}

	public Float getItemId() {
		return itemId;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public String getItemCategory() {
		return itemCategory;
	}

	public void setVersionNum(String versionNum) {
		this.versionNum = versionNum;
	}

	public String getVersionNum() {
		return versionNum;
	}

	public void setDrawingNumber(String drawingNumber) {
		this.drawingNumber = drawingNumber;
	}

	public String getDrawingNumber() {
		return drawingNumber;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}

	public String getEnableFlag() {
		return enableFlag;
	}

	public void setHistoryNum(Float historyNum) {
		this.historyNum = historyNum;
	}

	public Float getHistoryNum() {
		return historyNum;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

}
