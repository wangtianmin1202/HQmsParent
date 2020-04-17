package com.hand.spc.pspc_count_sample_data.dto;

import javax.persistence.Transient;
import java.util.Date;

public class CountSampleDataVO {
    private Long countSampleDataId;

    private Long attachmentGroupId;

    private Long ceGroupId;

    private Long ceParameterId;

    private Double sampleValueCount;

    /**
     * 样本时间
     */
    private Date sampleTime;

    private Double unqualifiedQuantity;

    private Date creationDate;

    /**
     * 附着对象组描述
     */
    @Transient
    private String attachmentGroupDescription;

    /**
     * 控制要素组编码
     */
    @Transient
    private String ceGroup;

    /**
     * 控制要素名称
     */
    @Transient
    private String ceParameterName;

    public Long getCountSampleDataId() {
        return countSampleDataId;
    }

    public void setCountSampleDataId(Long countSampleDataId) {
        this.countSampleDataId = countSampleDataId;
    }

    public Long getAttachmentGroupId() {
        return attachmentGroupId;
    }

    public void setAttachmentGroupId(Long attachmentGroupId) {
        this.attachmentGroupId = attachmentGroupId;
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

    public Double getSampleValueCount() {
        return sampleValueCount;
    }

    public void setSampleValueCount(Double sampleValueCount) {
        this.sampleValueCount = sampleValueCount;
    }

    public Double getUnqualifiedQuantity() {
        return unqualifiedQuantity;
    }

    public void setUnqualifiedQuantity(Double unqualifiedQuantity) {
        this.unqualifiedQuantity = unqualifiedQuantity;
    }

    public Date getSampleTime() {
        return sampleTime;
    }

    public void setSampleTime(Date sampleTime) {
        this.sampleTime = sampleTime;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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

    public String getCeParameterName() {
        return ceParameterName;
    }

    public void setCeParameterName(String ceParameterName) {
        this.ceParameterName = ceParameterName;
    }
}
