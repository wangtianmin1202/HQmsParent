package com.hand.spc.pspc_entity.dto;

/**Auto Generated By Hap Code Generator**/

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hand.hap.system.dto.BaseDTO;
import java.util.Date;

@ExtensionAttribute(disable=true)
@Table(name = "PSPC_ENTITY")
public class Entity extends BaseDTO {

    public static final String FIELD_ENTITY_ID = "entityId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_ENTITY_CODE = "entityCode";
    public static final String FIELD_ENTITY_VERSION = "entityVersion";
    public static final String FIELD_CONTROL_DATE = "controlDate";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_ATTACHMENT_GROUP_ID = "attachmentGroupId";
    public static final String FIELD_CE_GROUP_ID = "ceGroupId";
    public static final String FIELD_CE_PARAMETER_ID = "ceParameterId";
    public static final String FIELD_CHART_ID = "chartId";
    public static final String FIELD_ENTITY_STATUS = "entityStatus";
    public static final String FIELD_ENTITY_NEW = "entityNew";


    @Id
    @GeneratedValue
    private Long entityId;

    private Long tenantId;

    private Long siteId;

    @Length(max = 30)
    private String entityCode;

    @Length(max = 30)
    private String entityVersion;

    private Date controlDate;

    @Length(max = 300)
    private String description;

    private Long attachmentGroupId;

    private Long ceGroupId;

    private Long ceParameterId;

    private Long chartId;

    @Length(max = 30)
    private String entityStatus;

    @Length(max = 10)
    private String entityNew;

    @Transient
    private String chartCode;

    @Transient
    private String ceParameter;//控制要素编码

    @Transient
    private String ceParameterName;//控制要素名称

    @Transient
    private String ceGroup;//控制要素组编码

    @Transient
    private String attachmentGroupDescription;//附着对象组描述

    //实体控制图复制id
    @Transient
    private Long ceCopy;

    public void setEntityId(Long entityId){
        this.entityId = entityId;
    }

    public Long getEntityId(){
        return entityId;
    }

    public void setTenantId(Long tenantId){
        this.tenantId = tenantId;
    }

    public Long getTenantId(){
        return tenantId;
    }

    public void setSiteId(Long siteId){
        this.siteId = siteId;
    }

    public Long getSiteId(){
        return siteId;
    }

    public void setEntityCode(String entityCode){
        this.entityCode = entityCode;
    }

    public String getEntityCode(){
        return entityCode;
    }

    public void setEntityVersion(String entityVersion){
        this.entityVersion = entityVersion;
    }

    public String getEntityVersion(){
        return entityVersion;
    }

    public void setControlDate(Date controlDate){
        this.controlDate = controlDate;
    }

    public Date getControlDate(){
        return controlDate;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }

    public void setAttachmentGroupId(Long attachmentGroupId){
        this.attachmentGroupId = attachmentGroupId;
    }

    public Long getAttachmentGroupId(){
        return attachmentGroupId;
    }

    public void setCeGroupId(Long ceGroupId){
        this.ceGroupId = ceGroupId;
    }

    public Long getCeGroupId(){
        return ceGroupId;
    }

    public void setCeParameterId(Long ceParameterId){
        this.ceParameterId = ceParameterId;
    }

    public Long getCeParameterId(){
        return ceParameterId;
    }

    public void setChartId(Long chartId){
        this.chartId = chartId;
    }

    public Long getChartId(){
        return chartId;
    }

    public void setEntityStatus(String entityStatus){
        this.entityStatus = entityStatus;
    }

    public String getEntityStatus(){
        return entityStatus;
    }

    public void setEntityNew(String entityNew){
        this.entityNew = entityNew;
    }

    public String getEntityNew(){
        return entityNew;
    }

    public String getCeParameter() {
        return ceParameter;
    }

    public void setCeParameter(String ceParameter) {
        this.ceParameter = ceParameter;
    }

    public String getCeParameterName() {
        return ceParameterName;
    }

    public void setCeParameterName(String ceParameterName) {
        this.ceParameterName = ceParameterName;
    }

    public String getCeGroup() {
        return ceGroup;
    }

    public void setCeGroup(String ceGroup) {
        this.ceGroup = ceGroup;
    }

    public String getAttachmentGroupDescription() {
        return attachmentGroupDescription;
    }

    public void setAttachmentGroupDescription(String attachmentGroupDescription) {
        this.attachmentGroupDescription = attachmentGroupDescription;
    }

    public String getChartCode() {
        return chartCode;
    }

    public void setChartCode(String chartCode) {
        this.chartCode = chartCode;
    }

    public Long getCeCopy() {
        return ceCopy;
    }

    public void setCeCopy(Long ceCopy) {
        this.ceCopy = ceCopy;
    }
}
