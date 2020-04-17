package com.hand.hqm.hqm_stan_op_item_h.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hand.hap.system.dto.BaseDTO;

@ExtensionAttribute(disable = false)
@Table(name = "HQM_STANDARD_OP_ITEM_H")
public class StandardOpItemH extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7489028895162697102L;

	@Id
	@GeneratedValue
	private Float headId;

	private Float hsoHeadId;

	@Length(max = 20)
	private String version;

	@Length(max = 1)
	private String enableFlag;

	private Float itemId;

	private String plantId;

	@Transient
	private String plantCode;

	@Transient
	private String prodLineCode;

	private Float prodLineId;

	private Float standardOpId;

	@Transient
	private String standardOpCode;

	@Transient
	private String standardOpDes;

	@Transient
	private String workstationCode;

	@Transient
	private String itemCode;

	@Transient
	private String itemDescriptions;

	private Float workstationId;

	private String sourceType;

	private String status;

	private Float timeLimit;

	private String itemEdition; // 物料版本 added by wtm 20191226
	
	//attriute1 模板变更信息

	public String getItemEdition() {
		return itemEdition;
	}

	public void setItemEdition(String itemEdition) {
		this.itemEdition = itemEdition;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Float getStandardOpId() {
		return standardOpId;
	}

	public void setStandardOpId(Float standardOpId) {
		this.standardOpId = standardOpId;
	}

	public Float getProdLineId() {
		return prodLineId;
	}

	public void setProdLineId(Float prodLineId) {
		this.prodLineId = prodLineId;
	}

	public Float getWorkstationId() {
		return workstationId;
	}

	public void setWorkstationId(Float workstationId) {
		this.workstationId = workstationId;
	}

	public Float getHsoHeadId() {
		return hsoHeadId;
	}

	public void setHsoHeadId(Float hsoHeadId) {
		this.hsoHeadId = hsoHeadId;
	}

	public String getPlantId() {
		return plantId;
	}

	public void setPlantId(String plantId) {
		this.plantId = plantId;
	}

	public String getPlantCode() {
		return plantCode;
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}

	public String getProdLineCode() {
		return prodLineCode;
	}

	public void setProdLineCode(String prodLineCode) {
		this.prodLineCode = prodLineCode;
	}

	public String getStandardOpCode() {
		return standardOpCode;
	}

	public void setStandardOpCode(String standardOpCode) {
		this.standardOpCode = standardOpCode;
	}

	public String getStandardOpDes() {
		return standardOpDes;
	}

	public void setStandardOpDes(String standardOpDes) {
		this.standardOpDes = standardOpDes;
	}

	public String getWorkstationCode() {
		return workstationCode;
	}

	public void setWorkstationCode(String workstationCode) {
		this.workstationCode = workstationCode;
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

	public void setHeadId(Float headId) {
		this.headId = headId;
	}

	public Float getHeadId() {
		return headId;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getVersion() {
		return version;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}

	public String getEnableFlag() {
		return enableFlag;
	}

	public void setItemId(Float itemId) {
		this.itemId = itemId;
	}

	public Float getItemId() {
		return itemId;
	}

}
