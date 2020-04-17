package com.hand.spc.repository.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import io.swagger.annotations.ApiModelProperty;

//"样本数据(计量)"
@ExtensionAttribute(disable=true)
@Table(name = "pspc_sample_data_wait")
public class SampleDataWaitR extends BaseDTO {
	@Id
    @GeneratedValue
    @ApiModelProperty(value = "样本数据ID")
    private Long sampleDataWaitId;//样本数据ID 主键
    @ApiModelProperty("租户ID")
    private Long tenantId;//租户ID
    @ApiModelProperty("站点ID")
    private Long siteId;//站点ID
    @ApiModelProperty("附着对象组ID")
    private Long attachmentGroupId;//附着对象组ID
    @ApiModelProperty("样本值")
    private BigDecimal sampleValue;//样本值
    @ApiModelProperty("样本时间")
    private Date sampleTime;//样本时间
    @ApiModelProperty("控制要素组ID")
    private Long ceGroupId;//控制要素组ID
    @ApiModelProperty("控制要素ID")
    private Long ceParameterId;//控制要素ID
    @ApiModelProperty("是否编辑")
    private String edited;//是否编辑
    @Transient
    private List<Long> sampleDataWaitIdList;//预处理样本数据ID集合

//    @Transient
//    @ApiModelProperty("附着对象组描述")
//    private String attachmentGroupDescription; //附着对象组描述
//    @Transient
//    @ApiModelProperty("控制要素组")
//    private String ceGroup;//控制要素组
//    @Transient
//    @ApiModelProperty("控制要素")
//    private String ceParameter;//控制要素
//    @Transient
//    @ApiModelProperty("样本开始时间")
//    private Date sampleBeginTime;//样本开始时间
//    @Transient
//    @ApiModelProperty("样本结束时间")
//    private Date sampleEndTime;//样本结束时间
//    @Transient
//    @ApiModelProperty("样本扩展属性值，逗号拼接")
//    private String sampleDataExtend;//样本扩展属性值，逗号拼接
//    @Transient
//    @ApiModelProperty(value = "样本数据扩展集合", hidden=false)
//    private List<SampleDataExtendWait> sampleDataExtendWaitList; //样本数据扩展集合

    public Long getSampleDataWaitId() {
        return sampleDataWaitId;
    }

    public void setSampleDataWaitId(Long sampleDataWaitId) {
        this.sampleDataWaitId = sampleDataWaitId;
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

    public Long getAttachmentGroupId() {
        return attachmentGroupId;
    }

    public void setAttachmentGroupId(Long attachmentGroupId) {
        this.attachmentGroupId = attachmentGroupId;
    }

    public BigDecimal getSampleValue() {
        return sampleValue;
    }

    public void setSampleValue(BigDecimal sampleValue) {
        this.sampleValue = sampleValue;
    }

    public Date getSampleTime() {
        return sampleTime;
    }

    public void setSampleTime(Date sampleTime) {
        this.sampleTime = sampleTime;
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

    public String getEdited() {
        return edited;
    }

    public void setEdited(String edited) {
        this.edited = edited;
    }

//    public String getAttachmentGroupDescription() {
//        return attachmentGroupDescription;
//    }
//
//    public void setAttachmentGroupDescription(String attachmentGroupDescription) {
//        this.attachmentGroupDescription = attachmentGroupDescription;
//    }
//
//    public String getCeGroup() {
//        return ceGroup;
//    }
//
//    public void setCeGroup(String ceGroup) {
//        this.ceGroup = ceGroup;
//    }
//
//    public String getCeParameter() {
//        return ceParameter;
//    }
//
//    public void setCeParameter(String ceParameter) {
//        this.ceParameter = ceParameter;
//    }
//
//    public List<SampleDataExtendWait> getSampleDataExtendWaitList() {
//        return sampleDataExtendWaitList;
//    }
//
//    public void setSampleDataExtendWaitList(List<SampleDataExtendWait> sampleDataExtendWaitList) {
//        this.sampleDataExtendWaitList = sampleDataExtendWaitList;
//    }
//
//    public Date getSampleBeginTime() {
//        return sampleBeginTime;
//    }
//
//    public void setSampleBeginTime(Date sampleBeginTime) {
//        this.sampleBeginTime = sampleBeginTime;
//    }
//
//    public Date getSampleEndTime() {
//        return sampleEndTime;
//    }
//
//    public void setSampleEndTime(Date sampleEndTime) {
//        this.sampleEndTime = sampleEndTime;
//    }
//
//    public String getSampleDataExtend() {
//        return sampleDataExtend;
//    }
//
//    public void setSampleDataExtend(String sampleDataExtend) {
//        this.sampleDataExtend = sampleDataExtend;
//    }


    public List<Long> getSampleDataWaitIdList() {
        return sampleDataWaitIdList;
    }

    public void setSampleDataWaitIdList(List<Long> sampleDataWaitIdList) {
        this.sampleDataWaitIdList = sampleDataWaitIdList;
    }
}
