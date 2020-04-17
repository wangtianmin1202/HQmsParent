package com.hand.hqm.hqm_msa_check_plan.dto;

import java.util.Date;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

@ExtensionAttribute(disable = true)
@Table(name = "HQM_MET_CHECK_PLAN")
public class MetCheckPlan extends BaseDTO {

	public static final String FIELD_CHECK_PLAN_ID = "checkPlanId";
	public static final String FIELD_CHECK_PLAN_CODE = "checkPlanCode";
	public static final String FIELD_PLANT_ID = "plantId";

	@Id
	@GeneratedValue
	private Float checkPlanId;

	@Length(max = 300)
	private String checkPlanCode;

	private Float plantId;
	
	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
	private Date creationDate;

	@Transient
	private String plantName;

	@Transient
	private String creationStartTime;// 编制日期从

	@Transient
	private String creationEndTime;// 编制日期至

	public String getCreationStartTime() {
		return creationStartTime;
	}

	public void setCreationStartTime(String creationStartTime) {
		this.creationStartTime = creationStartTime;
	}

	public String getCreationEndTime() {
		return creationEndTime;
	}

	public void setCreationEndTime(String creationEndTime) {
		this.creationEndTime = creationEndTime;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getPlantName() {
		return plantName;
	}

	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}

	public void setCheckPlanId(Float checkPlanId) {
		this.checkPlanId = checkPlanId;
	}

	public Float getCheckPlanId() {
		return checkPlanId;
	}

	public void setCheckPlanCode(String checkPlanCode) {
		this.checkPlanCode = checkPlanCode;
	}

	public String getCheckPlanCode() {
		return checkPlanCode;
	}

	public void setPlantId(Float plantId) {
		this.plantId = plantId;
	}

	public Float getPlantId() {
		return plantId;
	}

}