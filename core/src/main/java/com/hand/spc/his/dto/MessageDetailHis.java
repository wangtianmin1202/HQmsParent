package com.hand.spc.his.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import com.hand.hap.system.dto.BaseDTO;
@ExtensionAttribute(disable=true)
@Table(name = "PSPC_MESSAGE_DETAIL_HIS")
public class MessageDetailHis extends BaseDTO {

     public static final String FIELD_MESSAGE_DETAIL_HIS_ID = "messageDetailHisId";
     public static final String FIELD_MESSAGE_DETAIL_ID = "messageDetailId";
     public static final String FIELD_MESSAGE_ID = "messageId";
     public static final String FIELD_TENANT_ID = "tenantId";
     public static final String FIELD_SITE_ID = "siteId";
     public static final String FIELD_ENTITY_CODE = "entityCode";
     public static final String FIELD_ENTITY_VERSION = "entityVersion";
     public static final String FIELD_ELEMENT_CATEGORY = "elementCategory";
     public static final String FIELD_ELEMENT_CODE = "elementCode";
     public static final String FIELD_ELEMENT_DESCRIPTION = "elementDescription";
     public static final String FIELD_ELEMENT_STATUS = "elementStatus";
     public static final String FIELD_ELEMENT_VALUE_CODE = "elementValueCode";
     public static final String FIELD_ELEMENT_VALUE_DESCRIPTION = "elementValueDescription";

    @Id
    @GeneratedValue
     private Float messageDetailHisId;

     private Float messageDetailId;

     @Length(max = 100)
     private String messageId;

     private Float tenantId;

     private Float siteId;

     @Length(max = 50)
     private String entityCode;

     @Length(max = 50)
     private String entityVersion;

     @Length(max = 10)
     private String elementCategory;

     @Length(max = 50)
     private String elementCode;

     @Length(max = 120)
     private String elementDescription;

     @Length(max = 1)
     private String elementStatus;

     @Length(max = 4000)
     private String elementValueCode;

     @Length(max = 4000)
     private String elementValueDescription;


     public void setMessageDetailHisId(Float messageDetailHisId){
         this.messageDetailHisId = messageDetailHisId;
     }

     public Float getMessageDetailHisId(){
         return messageDetailHisId;
     }

     public void setMessageDetailId(Float messageDetailId){
         this.messageDetailId = messageDetailId;
     }

     public Float getMessageDetailId(){
         return messageDetailId;
     }

     public void setMessageId(String messageId){
         this.messageId = messageId;
     }

     public String getMessageId(){
         return messageId;
     }

     public void setTenantId(Float tenantId){
         this.tenantId = tenantId;
     }

     public Float getTenantId(){
         return tenantId;
     }

     public void setSiteId(Float siteId){
         this.siteId = siteId;
     }

     public Float getSiteId(){
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

     public void setElementCategory(String elementCategory){
         this.elementCategory = elementCategory;
     }

     public String getElementCategory(){
         return elementCategory;
     }

     public void setElementCode(String elementCode){
         this.elementCode = elementCode;
     }

     public String getElementCode(){
         return elementCode;
     }

     public void setElementDescription(String elementDescription){
         this.elementDescription = elementDescription;
     }

     public String getElementDescription(){
         return elementDescription;
     }

     public void setElementStatus(String elementStatus){
         this.elementStatus = elementStatus;
     }

     public String getElementStatus(){
         return elementStatus;
     }

     public void setElementValueCode(String elementValueCode){
         this.elementValueCode = elementValueCode;
     }

     public String getElementValueCode(){
         return elementValueCode;
     }

     public void setElementValueDescription(String elementValueDescription){
         this.elementValueDescription = elementValueDescription;
     }

     public String getElementValueDescription(){
         return elementValueDescription;
     }

     }
