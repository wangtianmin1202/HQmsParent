package com.hand.npi.npi_technology.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import com.hand.hap.system.dto.BaseDTO;

@ExtensionAttribute(disable = true)
@Table(name = "NPI_TECHNOLOGY_STANDARD_ACTION")
public class TechnologyStandardAction extends BaseDTO {

	public static final String FIELD_STANDARD_ACTION_ID = "standardActionId";
	public static final String FIELD_ACTION_NAME = "actionName";
	public static final String FIELD_STANDARD_WORKING_HOURS = "standardWorkingHours";
	public static final String FIELD_TECHNOLOGY_ACTION_NUMBER = "technologyActionNumber";

	@Id
	@GeneratedValue
	private Float standardActionId;

	/**
	 * 标准装配动作
	 */
	@Length(max = 240)
	private String actionName;

	/**
	 * 标准工时（S）
	 */
	private Float standardWorkingHours;

	/**
	 * 标准装配动作编码
	 */
	@Length(max = 240)
	private String technologyActionNumber;

	/**
	 * 计量/计数
	 */
	@Length(max = 240)
	private String meteringCount;

	/**
	 * 装配要求明细
	 */
	@Length(max = 240)
	private String assemblingDetail;

	/**
	 * 标准/测试方法
	 */
	@Length(max = 240)
	private String standardTestMethod;
	
	/**
	 * 动作类型
	 */
	@Length(max = 30)
    private String actionType;

	public void setStandardActionId(Float standardActionId) {
		this.standardActionId = standardActionId;
	}

	public Float getStandardActionId() {
		return standardActionId;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getActionName() {
		return actionName;
	}

	public void setStandardWorkingHours(Float standardWorkingHours) {
		this.standardWorkingHours = standardWorkingHours;
	}

	public Float getStandardWorkingHours() {
		return standardWorkingHours;
	}

	public void setTechnologyActionNumber(String technologyActionNumber) {
		this.technologyActionNumber = technologyActionNumber;
	}

	public String getTechnologyActionNumber() {
		return technologyActionNumber;
	}

	public String getMeteringCount() {
		return meteringCount;
	}

	public void setMeteringCount(String meteringCount) {
		this.meteringCount = meteringCount;
	}

	public String getAssemblingDetail() {
		return assemblingDetail;
	}

	public void setAssemblingDetail(String assemblingDetail) {
		this.assemblingDetail = assemblingDetail;
	}

	public String getStandardTestMethod() {
		return standardTestMethod;
	}

	public void setStandardTestMethod(String standardTestMethod) {
		this.standardTestMethod = standardTestMethod;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	
}
