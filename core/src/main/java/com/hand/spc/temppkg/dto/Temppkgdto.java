package com.hand.spc.temppkg.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import com.hand.hap.system.dto.BaseDTO;
@ExtensionAttribute(disable=true)
@Table(name = "PSPC_MESSAGE_TYPE")
public class Temppkgdto extends BaseDTO {

     public static final String FIELD_MESSAGE_TYPE_ID = "messageTypeId";
     public static final String FIELD_TENANT_ID = "tenantId";
     public static final String FIELD_SITE_ID = "siteId";
     public static final String FIELD_MESSAGE_TYPE_CODE = "messageTypeCode";
     public static final String FIELD_MESSAGE_TYPE_STATUS = "messageTypeStatus";


     @Id
     @GeneratedValue
     private Float messageTypeId;

     private Float tenantId;

     private Float siteId;

     @Length(max = 20)
     private String messageTypeCode;

     @Length(max = 1)
     private String messageTypeStatus;


     public void setMessageTypeId(Float messageTypeId){
         this.messageTypeId = messageTypeId;
     }

     public Float getMessageTypeId(){
         return messageTypeId;
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

     public void setMessageTypeCode(String messageTypeCode){
         this.messageTypeCode = messageTypeCode;
     }

     public String getMessageTypeCode(){
         return messageTypeCode;
     }

     public void setMessageTypeStatus(String messageTypeStatus){
         this.messageTypeStatus = messageTypeStatus;
     }

     public String getMessageTypeStatus(){
         return messageTypeStatus;
     }

     }
