package com.hand.spc.his.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import com.hand.hap.system.dto.BaseDTO;
@ExtensionAttribute(disable=true)
@Table(name = "PSPC_SAMPLE_SUBGROUP_REL_HIS")
public class SampleSubgroupRelHis extends BaseDTO {

     public static final String FIELD_SAMPLE_SUBGROUP_REL_HIS_ID = "sampleSubgroupRelHisId";
     public static final String FIELD_SAMPLE_SUBGROUP_REL_ID = "sampleSubgroupRelId";
     public static final String FIELD_TENANT_ID = "tenantId";
     public static final String FIELD_SITE_ID = "siteId";
     public static final String FIELD_SAMPLE_SUBGROUP_ID = "sampleSubgroupId";
     public static final String FIELD_SAMPLE_DATA_ID = "sampleDataId";
     public static final String FIELD_SAMPLE_VALUE = "sampleValue";
     public static final String FIELD_ENTITY_CODE = "entityCode";
     public static final String FIELD_ENTITY_VERSION = "entityVersion";

    @Id
    @GeneratedValue
     private Float sampleSubgroupRelHisId;

     private Float sampleSubgroupRelId;

     private Float tenantId;

     private Float siteId;

     private Float sampleSubgroupId;

     private Float sampleDataId;

     private Float sampleValue;

     @Length(max = 150)
     private String entityCode;

     @Length(max = 50)
     private String entityVersion;


     public void setSampleSubgroupRelHisId(Float sampleSubgroupRelHisId){
         this.sampleSubgroupRelHisId = sampleSubgroupRelHisId;
     }

     public Float getSampleSubgroupRelHisId(){
         return sampleSubgroupRelHisId;
     }

     public void setSampleSubgroupRelId(Float sampleSubgroupRelId){
         this.sampleSubgroupRelId = sampleSubgroupRelId;
     }

     public Float getSampleSubgroupRelId(){
         return sampleSubgroupRelId;
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

     public void setSampleSubgroupId(Float sampleSubgroupId){
         this.sampleSubgroupId = sampleSubgroupId;
     }

     public Float getSampleSubgroupId(){
         return sampleSubgroupId;
     }

     public void setSampleDataId(Float sampleDataId){
         this.sampleDataId = sampleDataId;
     }

     public Float getSampleDataId(){
         return sampleDataId;
     }

     public void setSampleValue(Float sampleValue){
         this.sampleValue = sampleValue;
     }

     public Float getSampleValue(){
         return sampleValue;
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

     }
