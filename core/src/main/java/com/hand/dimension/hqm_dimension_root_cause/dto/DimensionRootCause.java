package com.hand.dimension.hqm_dimension_root_cause.dto;

import java.util.Date;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hand.hap.system.dto.BaseDTO;

@ExtensionAttribute(disable = true)
@Table(name = "HQM_8D_ROOT_CAUSE")
public class DimensionRootCause extends BaseDTO {

	@Id
	@GeneratedValue
	private Float rcauseId;

	private Float step;

	private Float orderId;

	@Length(max = 1000)
	private String rootCause;

	private String hisRate;

	private String problemDescription;

	private String failureEffectCode;

	public String getProblemDescription() {
		return problemDescription;
	}

	public void setProblemDescription(String problemDescription) {
		this.problemDescription = problemDescription;
	}

	public String getFailureEffectCode() {
		return failureEffectCode;
	}

	public void setFailureEffectCode(String failureEffectCode) {
		this.failureEffectCode = failureEffectCode;
	}

	public String getHisRate() {
		return hisRate;
	}

	public void setHisRate(String hisRate) {
		this.hisRate = hisRate;
	}

	public void setStep(Float step) {
		this.step = step;
	}

	public Float getStep() {
		return step;
	}

	public void setRcauseId(Float rcauseId) {
		this.rcauseId = rcauseId;
	}

	public Float getRcauseId() {
		return rcauseId;
	}

	public void setOrderId(Float orderId) {
		this.orderId = orderId;
	}

	public Float getOrderId() {
		return orderId;
	}

	public void setRootCause(String rootCause) {
		this.rootCause = rootCause;
	}

	public String getRootCause() {
		return rootCause;
	}

}
