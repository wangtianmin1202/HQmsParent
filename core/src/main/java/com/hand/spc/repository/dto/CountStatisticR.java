package com.hand.spc.repository.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

//import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModelProperty;

/**
 * 计数型统计量表
 *
 * @author peng.hu04@hand-china.com 2019-06-20 11:01:57
 */
//"计数型统计量表"
@ExtensionAttribute(disable=true)
@Table(name = "pspc_count_statistic")
public class CountStatisticR extends BaseDTO {

    public static final String FIELD_COUNT_STATISTIC_ID = "countStatisticId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_COUNT_SAMPLE_DATA_ID = "countSampleDataId";
    public static final String FIELD_ENTITY_CODE = "entityCode";
    public static final String FIELD_ENTITY_VERSION = "entityVersion";
    public static final String FIELD_SUBGROUP_NUM = "subgroupNum";
    public static final String FIELD_SAMPLE_VALUE_COUNT = "sampleValueCount";
    public static final String FIELD_UNQUALIFIED_QUANTITY = "unqualifiedQuantity";
    public static final String FIELD_SAMPLE_TIME = "sampleTime";
    public static final String FIELD_UNQUALIFIED_PERCENT = "unqualifiedPercent";
    public static final String FIELD_UPPER_CONTROL_LIMIT = "upperControlLimit";
    public static final String FIELD_CENTER_LINE = "centerLine";
    public static final String FIELD_LOWER_CONTROL_LIMIT = "lowerControlLimit";
    public static final String FIELD_UPPER_SPEC_LIMIT = "upperSpecLimit";
    public static final String FIELD_SPEC_TARGET = "specTarget";
    public static final String FIELD_LOWER_SPEC_LIMIT = "lowerSpecLimit";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("表ID，主键")
    @Id
    @GeneratedValue
    private Long countStatisticId;
    @ApiModelProperty(value = "租户ID",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "站点ID",required = true)
    @NotNull
    private Long siteId;
    @ApiModelProperty(value = "样本数据ID",required = true)
    @NotNull
    private Long countSampleDataId;
    @ApiModelProperty(value = "SPC实体控制图",required = true)
    @NotNull
    private String entityCode;
   @ApiModelProperty(value = "SPC实体控制图版本")    
    private String entityVersion;
   @ApiModelProperty(value = "组号")    
    private Long subgroupNum;
   @ApiModelProperty(value = "样本数（计数)")    
    private BigDecimal sampleValueCount;
   @ApiModelProperty(value = "不合格数")    
    private BigDecimal unqualifiedQuantity;
   @ApiModelProperty(value = "样本时间")    
    private Date sampleTime;
   @ApiModelProperty(value = "不合格率/单位缺陷数")    
    private BigDecimal unqualifiedPercent;
   @ApiModelProperty(value = "控制上限")    
    private BigDecimal upperControlLimit;
   @ApiModelProperty(value = "中心线")    
    private BigDecimal centerLine;
   @ApiModelProperty(value = "控制下限")    
    private BigDecimal lowerControlLimit;
   @ApiModelProperty(value = "规格上限")    
    private BigDecimal upperSpecLimit;
   @ApiModelProperty(value = "目标值")    
    private BigDecimal specTarget;
   @ApiModelProperty(value = "规格下限")    
    private BigDecimal lowerSpecLimit;

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------
	@Transient
	private Double mainStatisticValue;//主图统计量值
	@Transient
	private String sampleDatas;//样本数据，样本时间+"/"+样本值 以逗号拼接
	@Transient
	private String insStatus;//样本数据(Y表示新增)

	//
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 表ID，主键
     */
	public Long getCountStatisticId() {
		return countStatisticId;
	}

	public void setCountStatisticId(Long countStatisticId) {
		this.countStatisticId = countStatisticId;
	}
    /**
     * @return 租户ID
     */
	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
    /**
     * @return 站点ID
     */
	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}
    /**
     * @return 样本数据ID
     */
	public Long getCountSampleDataId() {
		return countSampleDataId;
	}

	public void setCountSampleDataId(Long countSampleDataId) {
		this.countSampleDataId = countSampleDataId;
	}
    /**
     * @return SPC实体控制图
     */
	public String getEntityCode() {
		return entityCode;
	}

	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}
    /**
     * @return SPC实体控制图版本
     */
	public String getEntityVersion() {
		return entityVersion;
	}

	public void setEntityVersion(String entityVersion) {
		this.entityVersion = entityVersion;
	}
    /**
     * @return 组号
     */
	public Long getSubgroupNum() {
		return subgroupNum;
	}

	public void setSubgroupNum(Long subgroupNum) {
		this.subgroupNum = subgroupNum;
	}
    /**
     * @return 样本数（计数)
     */
	public BigDecimal getSampleValueCount() {
		return sampleValueCount;
	}

	public void setSampleValueCount(BigDecimal sampleValueCount) {
		this.sampleValueCount = sampleValueCount;
	}
    /**
     * @return 不合格数
     */
	public BigDecimal getUnqualifiedQuantity() {
		return unqualifiedQuantity;
	}

	public void setUnqualifiedQuantity(BigDecimal unqualifiedQuantity) {
		this.unqualifiedQuantity = unqualifiedQuantity;
	}
    /**
     * @return 样本时间
     */
	public Date getSampleTime() {
		return sampleTime;
	}

	public void setSampleTime(Date sampleTime) {
		this.sampleTime = sampleTime;
	}
    /**
     * @return 不合格率/单位缺陷数
     */
	public BigDecimal getUnqualifiedPercent() {
		return unqualifiedPercent;
	}

	public void setUnqualifiedPercent(BigDecimal unqualifiedPercent) {
		this.unqualifiedPercent = unqualifiedPercent;
	}
    /**
     * @return 控制上限
     */
	public BigDecimal getUpperControlLimit() {
		return upperControlLimit;
	}

	public void setUpperControlLimit(BigDecimal upperControlLimit) {
		this.upperControlLimit = upperControlLimit;
	}
    /**
     * @return 中心线
     */
	public BigDecimal getCenterLine() {
		return centerLine;
	}

	public void setCenterLine(BigDecimal centerLine) {
		this.centerLine = centerLine;
	}
    /**
     * @return 控制下限
     */
	public BigDecimal getLowerControlLimit() {
		return lowerControlLimit;
	}

	public void setLowerControlLimit(BigDecimal lowerControlLimit) {
		this.lowerControlLimit = lowerControlLimit;
	}
    /**
     * @return 规格上限
     */
	public BigDecimal getUpperSpecLimit() {
		return upperSpecLimit;
	}

	public void setUpperSpecLimit(BigDecimal upperSpecLimit) {
		this.upperSpecLimit = upperSpecLimit;
	}
    /**
     * @return 目标值
     */
	public BigDecimal getSpecTarget() {
		return specTarget;
	}

	public void setSpecTarget(BigDecimal specTarget) {
		this.specTarget = specTarget;
	}
    /**
     * @return 规格下限
     */
	public BigDecimal getLowerSpecLimit() {
		return lowerSpecLimit;
	}

	public void setLowerSpecLimit(BigDecimal lowerSpecLimit) {
		this.lowerSpecLimit = lowerSpecLimit;
	}

	public Double getMainStatisticValue() {
		return mainStatisticValue;
	}

	public void setMainStatisticValue(Double mainStatisticValue) {
		this.mainStatisticValue = mainStatisticValue;
	}

	public String getSampleDatas() {
		return sampleDatas;
	}

	public void setSampleDatas(String sampleDatas) {
		this.sampleDatas = sampleDatas;
	}

	public String getInsStatus() {
		return insStatus;
	}

	public void setInsStatus(String insStatus) {
		this.insStatus = insStatus;
	}
}
