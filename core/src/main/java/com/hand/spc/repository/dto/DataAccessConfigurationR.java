package com.hand.spc.repository.dto;

import java.math.BigDecimal;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.system.dto.BaseDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 数据接入配置表
 *
 * @author lu.liu03@hand-china.com 2019-07-03 14:48:24
 */
@ApiModel("数据接入配置表")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "pspc_data_access_configuration")
public class DataAccessConfigurationR extends BaseDTO {

//    public static final String FIELD_DATA_ACCESS_CONFIGURATION_ID = "dataAccessConfigurationId";
//    public static final String FIELD_TENANT_ID = "tenantId";
//    public static final String FIELD_SITE_ID = "siteId";
//    public static final String FIELD_CE_GROUP_ID = "ceGroupId";
//    public static final String FIELD_ATTACHMENT_GROUP_ID = "attachmentGroupId";
//    public static final String FIELD_CE_PARAMETER_ID = "ceParameterId";
//    public static final String FIELD_CONFIGURATION_STATUS = "configurationStatus";
//    public static final String FIELD_TIME_SAMPLING = "timeSampling";
//    public static final String FIELD_ISOMETRIC_SAMPLING = "isometricSampling";
//    public static final String FIELD_EFFECTIVE_TYPE = "effectiveType";
//    public static final String FIELD_SAMPLING_POSITION = "samplingPosition";
//    public static final String FIELD_DATA_UPPER_LIMIT = "dataUpperLimit";
//    public static final String FIELD_DATA_LOWER_LIMIT = "dataLowerLimit";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


	@ApiModelProperty("主键")
	@Id
	@GeneratedValue
	private Long dataAccessConfigurationId;
	@ApiModelProperty(value = "租户",required = true)
	@NotNull
	private Long tenantId;
	@ApiModelProperty(value = "站点",required = true)
	@NotNull
	private Long siteId;

	@ApiModelProperty("控制要素组ID")
	private Long ceGroupId;
	@ApiModelProperty("控制要素ID")
	private Long ceParameterId;
	@ApiModelProperty("附着对象组ID")
	private Long attachmentGroupId;


	@ApiModelProperty(value = "配置状态",required = true)
	@NotNull
	private String configurationStatus;
	@ApiModelProperty(value = "时间抽样")
	private BigDecimal timeSampling;
	@ApiModelProperty(value = "等距抽样")
	private BigDecimal isometricSampling;
	@ApiModelProperty(value = "有效类型")
	private String effectiveType;
	@ApiModelProperty(value = "抽样取值位置")
	private String samplingPosition;
	@ApiModelProperty(value = "数据过滤上限")
	private BigDecimal dataUpperLimit;
	@ApiModelProperty(value = "数据过滤下限")
	private BigDecimal dataLowerLimit;

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------


	/**
     * @return 主键
     */


	public Long getDataAccessConfigurationId() {
		return dataAccessConfigurationId;
	}

	public void setDataAccessConfigurationId(Long dataAccessConfigurationId) {
		this.dataAccessConfigurationId = dataAccessConfigurationId;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

	public Long getCeGroupId() {
		return ceGroupId;
	}

	public void setCeGroupId(Long ceGroupId) {
		this.ceGroupId = ceGroupId;
	}

	public Long getCeParameterId() {
		return ceParameterId;
	}

	public void setCeParameterId(Long ceParameterId) {
		this.ceParameterId = ceParameterId;
	}

	public Long getAttachmentGroupId() {
		return attachmentGroupId;
	}

	public void setAttachmentGroupId(Long attachmentGroupId) {
		this.attachmentGroupId = attachmentGroupId;
	}

	public String getConfigurationStatus() {
		return configurationStatus;
	}

	public void setConfigurationStatus(String configurationStatus) {
		this.configurationStatus = configurationStatus;
	}

	public BigDecimal getTimeSampling() {
		return timeSampling;
	}

	public void setTimeSampling(BigDecimal timeSampling) {
		this.timeSampling = timeSampling;
	}

	public BigDecimal getIsometricSampling() {
		return isometricSampling;
	}

	public void setIsometricSampling(BigDecimal isometricSampling) {
		this.isometricSampling = isometricSampling;
	}

	public String getEffectiveType() {
		return effectiveType;
	}

	public void setEffectiveType(String effectiveType) {
		this.effectiveType = effectiveType;
	}

	public String getSamplingPosition() {
		return samplingPosition;
	}

	public void setSamplingPosition(String samplingPosition) {
		this.samplingPosition = samplingPosition;
	}

	public BigDecimal getDataUpperLimit() {
		return dataUpperLimit;
	}

	public void setDataUpperLimit(BigDecimal dataUpperLimit) {
		this.dataUpperLimit = dataUpperLimit;
	}

	public BigDecimal getDataLowerLimit() {
		return dataLowerLimit;
	}

	public void setDataLowerLimit(BigDecimal dataLowerLimit) {
		this.dataLowerLimit = dataLowerLimit;
	}
}
