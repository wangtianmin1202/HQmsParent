package com.hand.spc.pspc_sample_data.view;

import java.io.Serializable;
import java.util.Date;

public class SampleDataQueryVO implements Serializable {

    private static final long serialVersionUID = -449062985472136980L;

    private Float sampleDataId; // 主键

    private Float tenantId; // 租户ID

    private Float siteId; // 站点ID

    private Float attachmentGroupId; // 附着对象组ID

    private String attachmentGroupDescription; // 附着对象组


    private Float sampleValue; // 样本值

    private Date sampleTime; // 样本时间

    private Float ceGroupId; // 控制要素组ID

    private String ceGroup; // 控制要素组

    private Float ceParameterId; // 控制要素ID

    private String ceParameter; // 控制要素

    private String edited; // 是否编辑

    private String startTime; // 样本时间从

    private String endTime; // 样本时间至

    private Date creationDate; // 创建时间

    private String extendAttribute; // 拓展数据


    /*
    用于扩展字段暂存
     */
    private String attribute1;
    private String attribute2;
    private String attribute3;
    private String attribute4;
    private String attribute5;
    private String attribute6;
    private String attribute7;
    private String attribute8;
    private String attribute9;
    private String attribute10;
    private String attribute11;
    private String attribute12;
    private String attribute13;
    private String attribute14;
    private String attribute15;

    /*
   用于扩展字段标题的暂存
    */
    private String attribute1Title;
    private String attribute2Title;
    private String attribute3Title;
    private String attribute4Title;
    private String attribute5Title;
    private String attribute6Title;
    private String attribute7Title;
    private String attribute8Title;
    private String attribute9Title;
    private String attribute10Title;
    private String attribute11Title;
    private String attribute12Title;
    private String attribute13Title;
    private String attribute14Title;
    private String attribute15Title;

    public String getExtendAttribute() {
        return extendAttribute;
    }

    public void setExtendAttribute(String extendAttribute) {
        this.extendAttribute = extendAttribute;
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

    public String getCeParameter() {
        return ceParameter;
    }

    public void setCeParameter(String ceParameter) {
        this.ceParameter = ceParameter;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Float getSampleDataId() {
        return sampleDataId;
    }

    public void setSampleDataId(Float sampleDataId) {
        this.sampleDataId = sampleDataId;
    }

    public Float getTenantId() {
        return tenantId;
    }

    public void setTenantId(Float tenantId) {
        this.tenantId = tenantId;
    }

    public Float getSiteId() {
        return siteId;
    }

    public void setSiteId(Float siteId) {
        this.siteId = siteId;
    }

    public Float getAttachmentGroupId() {
        return attachmentGroupId;
    }

    public void setAttachmentGroupId(Float attachmentGroupId) {
        this.attachmentGroupId = attachmentGroupId;
    }

    public Float getSampleValue() {
        return sampleValue;
    }

    public void setSampleValue(Float sampleValue) {
        this.sampleValue = sampleValue;
    }

    public Date getSampleTime() {
        return sampleTime;
    }

    public void setSampleTime(Date sampleTime) {
        this.sampleTime = sampleTime;
    }

    public Float getCeGroupId() {
        return ceGroupId;
    }

    public void setCeGroupId(Float ceGroupId) {
        this.ceGroupId = ceGroupId;
    }

    public Float getCeParameterId() {
        return ceParameterId;
    }

    public void setCeParameterId(Float ceParameterId) {
        this.ceParameterId = ceParameterId;
    }

    public String getEdited() {
        return edited;
    }

    public void setEdited(String edited) {
        this.edited = edited;
    }

    public String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    public String getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }

    public String getAttribute4() {
        return attribute4;
    }

    public void setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
    }

    public String getAttribute5() {
        return attribute5;
    }

    public void setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
    }

    public String getAttribute6() {
        return attribute6;
    }

    public void setAttribute6(String attribute6) {
        this.attribute6 = attribute6;
    }

    public String getAttribute7() {
        return attribute7;
    }

    public void setAttribute7(String attribute7) {
        this.attribute7 = attribute7;
    }

    public String getAttribute8() {
        return attribute8;
    }

    public void setAttribute8(String attribute8) {
        this.attribute8 = attribute8;
    }

    public String getAttribute9() {
        return attribute9;
    }

    public void setAttribute9(String attribute9) {
        this.attribute9 = attribute9;
    }

    public String getAttribute10() {
        return attribute10;
    }

    public void setAttribute10(String attribute10) {
        this.attribute10 = attribute10;
    }

    public String getAttribute11() {
        return attribute11;
    }

    public void setAttribute11(String attribute11) {
        this.attribute11 = attribute11;
    }

    public String getAttribute12() {
        return attribute12;
    }

    public void setAttribute12(String attribute12) {
        this.attribute12 = attribute12;
    }

    public String getAttribute13() {
        return attribute13;
    }

    public void setAttribute13(String attribute13) {
        this.attribute13 = attribute13;
    }

    public String getAttribute14() {
        return attribute14;
    }

    public void setAttribute14(String attribute14) {
        this.attribute14 = attribute14;
    }

    public String getAttribute15() {
        return attribute15;
    }

    public void setAttribute15(String attribute15) {
        this.attribute15 = attribute15;
    }

    public String getAttribute1Title() {
        return attribute1Title;
    }

    public void setAttribute1Title(String attribute1Title) {
        this.attribute1Title = attribute1Title;
    }

    public String getAttribute2Title() {
        return attribute2Title;
    }

    public void setAttribute2Title(String attribute2Title) {
        this.attribute2Title = attribute2Title;
    }

    public String getAttribute3Title() {
        return attribute3Title;
    }

    public void setAttribute3Title(String attribute3Title) {
        this.attribute3Title = attribute3Title;
    }

    public String getAttribute4Title() {
        return attribute4Title;
    }

    public void setAttribute4Title(String attribute4Title) {
        this.attribute4Title = attribute4Title;
    }

    public String getAttribute5Title() {
        return attribute5Title;
    }

    public void setAttribute5Title(String attribute5Title) {
        this.attribute5Title = attribute5Title;
    }

    public String getAttribute6Title() {
        return attribute6Title;
    }

    public void setAttribute6Title(String attribute6Title) {
        this.attribute6Title = attribute6Title;
    }

    public String getAttribute7Title() {
        return attribute7Title;
    }

    public void setAttribute7Title(String attribute7Title) {
        this.attribute7Title = attribute7Title;
    }

    public String getAttribute8Title() {
        return attribute8Title;
    }

    public void setAttribute8Title(String attribute8Title) {
        this.attribute8Title = attribute8Title;
    }

    public String getAttribute9Title() {
        return attribute9Title;
    }

    public void setAttribute9Title(String attribute9Title) {
        this.attribute9Title = attribute9Title;
    }

    public String getAttribute10Title() {
        return attribute10Title;
    }

    public void setAttribute10Title(String attribute10Title) {
        this.attribute10Title = attribute10Title;
    }

    public String getAttribute11Title() {
        return attribute11Title;
    }

    public void setAttribute11Title(String attribute11Title) {
        this.attribute11Title = attribute11Title;
    }

    public String getAttribute12Title() {
        return attribute12Title;
    }

    public void setAttribute12Title(String attribute12Title) {
        this.attribute12Title = attribute12Title;
    }

    public String getAttribute13Title() {
        return attribute13Title;
    }

    public void setAttribute13Title(String attribute13Title) {
        this.attribute13Title = attribute13Title;
    }

    public String getAttribute14Title() {
        return attribute14Title;
    }

    public void setAttribute14Title(String attribute14Title) {
        this.attribute14Title = attribute14Title;
    }

    public String getAttribute15Title() {
        return attribute15Title;
    }

    public void setAttribute15Title(String attribute15Title) {
        this.attribute15Title = attribute15Title;
    }
}
