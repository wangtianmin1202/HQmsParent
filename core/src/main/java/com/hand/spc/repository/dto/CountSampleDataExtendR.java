package com.hand.spc.repository.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.system.dto.BaseDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 样本数据拓展表(计数)
 *
 * @author peng.hu04@hand-china.com 2019-06-20 11:02:07
 */
@ApiModel("样本数据拓展表(计数)")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "pspc_count_sample_data_extend")
public class CountSampleDataExtendR extends BaseDTO {

    public static final String FIELD_COUNT_SAMPLE_DATA_EXTEND_ID = "countSampleDataExtendId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_COUNT_SAMPLE_DATA_ID = "countSampleDataId";
    public static final String FIELD_EXTEND_ATTRIBUTE = "extendAttribute";
    public static final String FIELD_EXTEND_VALUE = "extendValue";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("表ID，主键")
    @Id
    @GeneratedValue
    private Long countSampleDataExtendId;
    @ApiModelProperty(value = "租户ID",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "站点ID",required = true)
    @NotNull
    private Long siteId;
    @ApiModelProperty(value = "计数型样本数据ID",required = true)
    @NotNull
    private Long countSampleDataId;
   @ApiModelProperty(value = "拓展属性")    
    private String extendAttribute;
   @ApiModelProperty(value = "拓展值")    
    private String extendValue;

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 表ID，主键
     */
	public Long getCountSampleDataExtendId() {
		return countSampleDataExtendId;
	}

	public void setCountSampleDataExtendId(Long countSampleDataExtendId) {
		this.countSampleDataExtendId = countSampleDataExtendId;
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
     * @return 拓展属性
     */
	public String getExtendAttribute() {
		return extendAttribute;
	}

	public void setExtendAttribute(String extendAttribute) {
		this.extendAttribute = extendAttribute;
	}
    /**
     * @return 拓展值
     */
	public String getExtendValue() {
		return extendValue;
	}

	public void setExtendValue(String extendValue) {
		this.extendValue = extendValue;
	}

}
