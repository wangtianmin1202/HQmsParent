package com.hand.spc.pspc_ooc.view;

import com.hand.spc.pspc_ooc.dto.Ooc;

public class OocReportVO extends Ooc {

    private static final long serialVersionUID = 8560880225519560585L;

    private Long ceGroupId;//控制要素组ID

    private String ceGroup;//控制要素组

    private String ceGroupDesc;//控制要素组描述

    private Long ceParameterId;//控制要素ID

    private String ceParameter;//控制要素

    private String ceParameterName;//控制要素名

    private Long attachmentGroupId;//附着对象组ID

    private String attachmentGroup;//附着对象组

    private String attachmentGroupDesc;//附着对象组描述

    private String startDate;//开始时间

    private String endDate;//结束时间

    private Long entityId;//实体控制图ID

    private String entityDesc;//实体控制图名称

    private Long chartId;//控制图ID

    private String chartCode;//控制图

    private String chartDesc;//控制图描述

    private String chartType;//控制图类型

    private String classifyGroupDesc;//原因分类组

    private String classifyDesc;//原因分类项

    private String creationDateStr;//创建时间

    private String messageTypeCode;//消息类型

    public Long getCeGroupId() {
        return ceGroupId;
    }

    public void setCeGroupId(Long ceGroupId) {
        this.ceGroupId = ceGroupId;
    }

    public String getCeGroup() {
        return ceGroup;
    }

    public void setCeGroup(String ceGroup) {
        this.ceGroup = ceGroup;
    }

    public Long getCeParameterId() {
        return ceParameterId;
    }

    public void setCeParameterId(Long ceParameterId) {
        this.ceParameterId = ceParameterId;
    }

    public String getCeParameter() {
        return ceParameter;
    }

    public void setCeParameter(String ceParameter) {
        this.ceParameter = ceParameter;
    }

    public Long getAttachmentGroupId() {
        return attachmentGroupId;
    }

    public void setAttachmentGroupId(Long attachmentGroupId) {
        this.attachmentGroupId = attachmentGroupId;
    }

    public String getAttachmentGroup() {
        return attachmentGroup;
    }

    public void setAttachmentGroup(String attachmentGroup) {
        this.attachmentGroup = attachmentGroup;
    }

    public String getCeGroupDesc() {
        return ceGroupDesc;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setCeGroupDesc(String ceGroupDesc) {
        this.ceGroupDesc = ceGroupDesc;
    }

    public String getCeParameterName() {
        return ceParameterName;
    }

    public void setCeParameterName(String ceParameterName) {
        this.ceParameterName = ceParameterName;
    }

    public String getAttachmentGroupDesc() {
        return attachmentGroupDesc;
    }

    public void setAttachmentGroupDesc(String attachmentGroupDesc) {
        this.attachmentGroupDesc = attachmentGroupDesc;
    }

    public String getEntityDesc() {
        return entityDesc;
    }

    public void setEntityDesc(String entityDesc) {
        this.entityDesc = entityDesc;
    }

    public Long getChartId() {
        return chartId;
    }

    public void setChartId(Long chartId) {
        this.chartId = chartId;
    }

    public String getChartCode() {
        return chartCode;
    }

    public void setChartCode(String chartCode) {
        this.chartCode = chartCode;
    }

    public String getChartDesc() {
        return chartDesc;
    }

    public void setChartDesc(String chartDesc) {
        this.chartDesc = chartDesc;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public String getClassifyGroupDesc() {
        return classifyGroupDesc;
    }

    public void setClassifyGroupDesc(String classifyGroupDesc) {
        this.classifyGroupDesc = classifyGroupDesc;
    }

    public String getClassifyDesc() {
        return classifyDesc;
    }

    public void setClassifyDesc(String classifyDesc) {
        this.classifyDesc = classifyDesc;
    }

    public String getCreationDateStr() {
        return creationDateStr;
    }

    public void setCreationDateStr(String creationDateStr) {
        this.creationDateStr = creationDateStr;
    }

    public String getMessageTypeCode() {
        return messageTypeCode;
    }

    public void setMessageTypeCode(String messageTypeCode) {
        this.messageTypeCode = messageTypeCode;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
}