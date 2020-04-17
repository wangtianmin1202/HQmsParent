package com.hand.dimension.hqm_dimension_initiated_actions.dto;

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
@Table(name = "HQM_8D_INITIATED_ACTIONS")
public class DimensionInitiatedActions extends BaseDTO {

	private Float step;

	@Id
	@GeneratedValue
	private Float actionId;

	private Float orderId;

	private Float pcActionId;

	private Date planExecutionTime;

	private Date actualExecutionTime;

	@Length(max = 30)
	private String actionStatus;

	@Length(max = 300)
	private String remark;

	@Length(max = 1)
	private String enableFlag;

	@Length(max = 300)
	private String evaluation;
	@Transient
	private Float userId;
	@Transient
	private String improvingAction;
	@Transient
	private String actionDescription;
	@Transient
	private String userName;
	@Transient
	private String fileUrl;
	@Transient
	private Float fileId;
	@Transient
	private String employeeName;
	
	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Float getFileId() {
		return fileId;
	}

	public void setFileId(Float fileId) {
		this.fileId = fileId;
	}

	public Float getUserId() {
		return userId;
	}

	public void setUserId(Float userId) {
		this.userId = userId;
	}

	public String getImprovingAction() {
		return improvingAction;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public void setImprovingAction(String improvingAction) {
		this.improvingAction = improvingAction;
	}

	public String getActionDescription() {
		return actionDescription;
	}

	public void setActionDescription(String actionDescription) {
		this.actionDescription = actionDescription;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setStep(Float step) {
		this.step = step;
	}

	public Float getStep() {
		return step;
	}

	public void setActionId(Float actionId) {
		this.actionId = actionId;
	}

	public Float getActionId() {
		return actionId;
	}

	public void setOrderId(Float orderId) {
		this.orderId = orderId;
	}

	public Float getOrderId() {
		return orderId;
	}

	public void setPcActionId(Float pcActionId) {
		this.pcActionId = pcActionId;
	}

	public Float getPcActionId() {
		return pcActionId;
	}

	public void setPlanExecutionTime(Date planExecutionTime) {
		this.planExecutionTime = planExecutionTime;
	}

	public Date getPlanExecutionTime() {
		return planExecutionTime;
	}

	public void setActualExecutionTime(Date actualExecutionTime) {
		this.actualExecutionTime = actualExecutionTime;
	}

	public Date getActualExecutionTime() {
		return actualExecutionTime;
	}

	public void setActionStatus(String actionStatus) {
		this.actionStatus = actionStatus;
	}

	public String getActionStatus() {
		return actionStatus;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemark() {
		return remark;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}

	public String getEnableFlag() {
		return enableFlag;
	}

	public void setEvaluation(String evaluation) {
		this.evaluation = evaluation;
	}

	public String getEvaluation() {
		return evaluation;
	}

}
