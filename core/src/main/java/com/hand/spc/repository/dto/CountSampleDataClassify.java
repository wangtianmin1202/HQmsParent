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
 * 分类项行表(计数)
 *
 * @author peng.hu04@hand-china.com 2019-06-20 11:02:29
 */
@ApiModel("分类项行表(计数)")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "pspc_count_sample_data_classify")
public class CountSampleDataClassify extends BaseDTO {

    public static final String FIELD_COUNT_SAMPLE_DATA_CLASSIFY_ID = "countSampleDataClassifyId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_COUNT_SAMPLE_DATA_ID = "countSampleDataId";
    public static final String FIELD_CLASSIFY_ID = "classifyId";
    public static final String FIELD_CLASSIFY_COUNT_VALUE = "classifyCountValue";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("表ID，主键")
    @Id
    @GeneratedValue
    private Long countSampleDataClassifyId;
    @ApiModelProperty(value = "租户ID",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "站点ID",required = true)
    @NotNull
    private Long siteId;
    @ApiModelProperty(value = "计数型样本数据ID",required = true)
    @NotNull
    private Long countSampleDataId;
    @ApiModelProperty(value = "分类项ID",required = true)
    @NotNull
    private Long classifyId;
   @ApiModelProperty(value = "分类项值")    
    private BigDecimal classifyCountValue;

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 表ID，主键
     */
	public Long getCountSampleDataClassifyId() {
		return countSampleDataClassifyId;
	}

	public void setCountSampleDataClassifyId(Long countSampleDataClassifyId) {
		this.countSampleDataClassifyId = countSampleDataClassifyId;
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
     * @return 计数型样本数据ID
     */
	public Long getCountSampleDataId() {
		return countSampleDataId;
	}

	public void setCountSampleDataId(Long countSampleDataId) {
		this.countSampleDataId = countSampleDataId;
	}

    /**
     * @return 分类项ID
     */
	public Long getClassifyId() {
		return classifyId;
	}

	public void setClassifyId(Long classifyId) {
		this.classifyId = classifyId;
	}
    /**
     * @return 分类项值
     */
	public BigDecimal getClassifyCountValue() {
		return classifyCountValue;
	}

	public void setClassifyCountValue(BigDecimal classifyCountValue) {
		this.classifyCountValue = classifyCountValue;
	}

}
