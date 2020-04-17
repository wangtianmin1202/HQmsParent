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
 * 样本数据(计数)预处理
 *
 * @author peng.hu04@hand-china.com 2019-06-20 11:02:18
 */
@ApiModel("样本数据(计数)预处理")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "pspc_count_sample_data_wait")
public class CountSampleDataWaitR extends BaseDTO {

    public static final String FIELD_COUNT_SAMPLE_DATA_WAIT_ID = "countSampleDataWaitId";
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
    private Long countSampleDataWaitId;
    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "站点ID", required = true)
    @NotNull
    private Long siteId;
    @ApiModelProperty(value = "附着对象组ID", required = true)
    @NotNull
    private Long attachmentGroupId;
    @ApiModelProperty(value = "控制要素组ID", required = true)
    @NotNull
    private Long ceGroupId;
    @ApiModelProperty(value = "控制要素ID", required = true)
    @NotNull
    private Long ceParameterId;
    @ApiModelProperty(value = "样本值（计数）")
    private BigDecimal sampleValueCount;
    @ApiModelProperty(value = "样本时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sampleTime;
    @ApiModelProperty(value = "不合格数")
    private BigDecimal unqualifiedQuantity;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------
    @Transient
    private List<Long> countSampleDataWaitIdList;//预处理样本数据ID集合
    @Transient
    private List<CountSampleDataClassifyDTO> countSampleDataClassifyList;//分类项
    @Transient
    private List<CountSampleDataExtendR> countSampleDataExtendList;//扩展属性

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 表ID，主键
     */
    public Long getCountSampleDataWaitId() {
        return countSampleDataWaitId;
    }

    public void setCountSampleDataWaitId(Long countSampleDataWaitId) {
        this.countSampleDataWaitId = countSampleDataWaitId;
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

    public List<Long> getCountSampleDataWaitIdList() {
        return countSampleDataWaitIdList;
    }

    public void setCountSampleDataWaitIdList(List<Long> countSampleDataWaitIdList) {
        this.countSampleDataWaitIdList = countSampleDataWaitIdList;
    }
    
    public List<CountSampleDataClassifyDTO> getCountSampleDataClassifyList() {
        return countSampleDataClassifyList;
    }

    public void setCountSampleDataClassifyList(List<CountSampleDataClassifyDTO> countSampleDataClassifyList) {
        this.countSampleDataClassifyList = countSampleDataClassifyList;
    }

    public List<CountSampleDataExtendR> getCountSampleDataExtendList() {
        return countSampleDataExtendList;
    }

    public void setCountSampleDataExtendList(List<CountSampleDataExtendR> countSampleDataExtendList) {
        this.countSampleDataExtendList = countSampleDataExtendList;
    }
}
