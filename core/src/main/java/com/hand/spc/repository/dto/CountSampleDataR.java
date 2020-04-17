package com.hand.spc.repository.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.system.dto.BaseDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 样本数据(计数)
 *
 * @author peng.hu04@hand-china.com 2019-06-20 11:02:38
 */
@ApiModel("样本数据(计数)")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "pspc_count_sample_data")
public class CountSampleDataR extends BaseDTO {

    public static final String FIELD_COUNT_SAMPLE_DATA_ID = "countSampleDataId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_ATTACHMENT_GROUP_ID = "attachmentGroupId";
    public static final String FIELD_CE_GROUP_ID = "ceGroupId";
    public static final String FIELD_CE_PARAMETER_ID = "ceParameterId";
    public static final String FIELD_SAMPLE_VALUE_COUNT = "sampleValueCount";
    public static final String FIELD_SAMPLE_TIME = "sampleTime";
    public static final String FIELD_UNQUALIFIED_QUANTITY = "unqualifiedQuantity";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("表ID，主键")
    @Id
    @GeneratedValue
    private Long countSampleDataId;
    @ApiModelProperty(value = "租户ID",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "站点ID",required = true)
    @NotNull
    private Long siteId;
    @ApiModelProperty(value = "附着对象组ID",required = true)
    @NotNull
    private Long attachmentGroupId;
    @ApiModelProperty(value = "控制要素组ID",required = true)
    @NotNull
    private Long ceGroupId;
    @ApiModelProperty(value = "控制要素ID",required = true)
    @NotNull
    private Long ceParameterId;
   @ApiModelProperty(value = "样本值（计数）")    
    private BigDecimal sampleValueCount;
   @ApiModelProperty(value = "样本时间")    
    private Date sampleTime;
   @ApiModelProperty(value = "不合格数")    
    private BigDecimal unqualifiedQuantity;

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------

	@ApiModelProperty(value = "是否显示分类项")
	@Transient
	private Boolean showClassify;

	@ApiModelProperty(value = "分类项")
	@Transient
	private List<CountSampleDataClassify> countSampleDataClassifyList;//分类项
	@ApiModelProperty(value = "扩展属性")
	@Transient
	private List<CountSampleDataExtendR> countSampleDataExtendList;//扩展属性


	@ApiModelProperty(value = "附着对象组描述")
	@Transient
	private String attachmentGroupDescription;

	@ApiModelProperty(value = "控制要素组描述")
	@Transient
	private String ceGroup;

	@ApiModelProperty(value = "控制要素描述")
	@Transient
	private String ceParameter;

	@ApiModelProperty(value = "开始时间")
	@Transient
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date sampleBeginTime;

	@ApiModelProperty(value = "结束时间")
	@Transient
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date sampleEndTime;

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 表ID，主键
     */
	public Long getCountSampleDataId() {
		return countSampleDataId;
	}

	public void setCountSampleDataId(Long countSampleDataId) {
		this.countSampleDataId = countSampleDataId;
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
     * @return 附着对象组ID
     */
	public Long getAttachmentGroupId() {
		return attachmentGroupId;
	}

	public void setAttachmentGroupId(Long attachmentGroupId) {
		this.attachmentGroupId = attachmentGroupId;
	}
    /**
     * @return 控制要素组ID
     */
	public Long getCeGroupId() {
		return ceGroupId;
	}

	public void setCeGroupId(Long ceGroupId) {
		this.ceGroupId = ceGroupId;
	}
    /**
     * @return 控制要素ID
     */
	public Long getCeParameterId() {
		return ceParameterId;
	}

	public void setCeParameterId(Long ceParameterId) {
		this.ceParameterId = ceParameterId;
	}
    /**
     * @return 样本值（计数）
     */
	public BigDecimal getSampleValueCount() {
		return sampleValueCount;
	}

	public void setSampleValueCount(BigDecimal sampleValueCount) {
		this.sampleValueCount = sampleValueCount;
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
     * @return 不合格数
     */
	public BigDecimal getUnqualifiedQuantity() {
		return unqualifiedQuantity;
	}

	public void setUnqualifiedQuantity(BigDecimal unqualifiedQuantity) {
		this.unqualifiedQuantity = unqualifiedQuantity;
	}

	public void setCountSampleDataClassifyList(List<CountSampleDataClassify> countSampleDataClassifyList) {
		this.countSampleDataClassifyList = countSampleDataClassifyList;
	}

	public List<CountSampleDataExtendR> getCountSampleDataExtendList() {
		return countSampleDataExtendList;
	}

	public void setCountSampleDataExtendList(List<CountSampleDataExtendR> countSampleDataExtendList) {
		this.countSampleDataExtendList = countSampleDataExtendList;
	}

	public String getAttachmentGroupDescription() {
		return attachmentGroupDescription;
	}

	public void setAttachmentGroupDescription(String attachmentGroupDescription) {
		this.attachmentGroupDescription = attachmentGroupDescription;
	}

	public String getCeGroup() {
		return ceGroup;
	}

	public void setCeGroup(String ceGroup) {
		this.ceGroup = ceGroup;
	}

	public String getCeParameter() {
		return ceParameter;
	}

	public void setCeParameter(String ceParameter) {
		this.ceParameter = ceParameter;
	}

	public Date getSampleBeginTime() {
		return sampleBeginTime;
	}

	public void setSampleBeginTime(Date sampleBeginTime) {
		this.sampleBeginTime = sampleBeginTime;
	}

	public Date getSampleEndTime() {
		return sampleEndTime;
	}

	public void setSampleEndTime(Date sampleEndTime) {
		this.sampleEndTime = sampleEndTime;
	}

	public Boolean getShowClassify() {
		return showClassify;
	}

	public void setShowClassify(Boolean showClassify) {
		this.showClassify = showClassify;
	}

	public List<CountSampleDataClassify> getCountSampleDataClassifyList() {
		return countSampleDataClassifyList;
	}
}
