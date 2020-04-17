package com.hand.spc.his.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import com.hand.hap.system.dto.BaseDTO;
import java.util.Date;
@ExtensionAttribute(disable=true)
@Table(name = "PSPC_SAMPLE_SUBGROUP_HIS")
public class SampleSubgroupHis extends BaseDTO {

     public static final String FIELD_SAMPLE_SUBGROUP_HIS_ID = "sampleSubgroupHisId";
     public static final String FIELD_SAMPLE_SUBGROUP_ID = "sampleSubgroupId";
     public static final String FIELD_TENANT_ID = "tenantId";
     public static final String FIELD_SITE_ID = "siteId";
     public static final String FIELD_SUBGROUP_NUM = "subgroupNum";
     public static final String FIELD_ENTITY_CODE = "entityCode";
     public static final String FIELD_ENTITY_VERSION = "entityVersion";
     public static final String FIELD_SUBGROUP_SIZE = "subgroupSize";
     public static final String FIELD_EXIST_NUM = "existNum";
     public static final String FIELD_SAMPLE_SUBGROUP_TIME = "sampleSubgroupTime";
     public static final String FIELD_SAMPLE_CALCULATE_STATUS = "sampleCalculateStatus";
     public static final String FIELD_SAMPLE_MODIFIED = "sampleModified";

    @Id
    @GeneratedValue
     private Float sampleSubgroupHisId;

     private Float sampleSubgroupId;

     private Float tenantId;

     private Float siteId;

     private Float subgroupNum;

     @Length(max = 150)
     private String entityCode;

     @Length(max = 50)
     private String entityVersion;

     private Float subgroupSize;

     private Float existNum;

     private Date sampleSubgroupTime;

     @Length(max = 30)
     private String sampleCalculateStatus;

     @Length(max = 10)
     private String sampleModified;


     public void setSampleSubgroupHisId(Float sampleSubgroupHisId){
         this.sampleSubgroupHisId = sampleSubgroupHisId;
     }

     public Float getSampleSubgroupHisId(){
         return sampleSubgroupHisId;
     }

     public void setSampleSubgroupId(Float sampleSubgroupId){
         this.sampleSubgroupId = sampleSubgroupId;
     }

     public Float getSampleSubgroupId(){
         return sampleSubgroupId;
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

     public void setSubgroupNum(Float subgroupNum){
         this.subgroupNum = subgroupNum;
     }

     public Float getSubgroupNum(){
         return subgroupNum;
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

     public void setSubgroupSize(Float subgroupSize){
         this.subgroupSize = subgroupSize;
     }

     public Float getSubgroupSize(){
         return subgroupSize;
     }

     public void setExistNum(Float existNum){
         this.existNum = existNum;
     }

     public Float getExistNum(){
         return existNum;
     }

     public void setSampleSubgroupTime(Date sampleSubgroupTime){
         this.sampleSubgroupTime = sampleSubgroupTime;
     }

     public Date getSampleSubgroupTime(){
         return sampleSubgroupTime;
     }

     public void setSampleCalculateStatus(String sampleCalculateStatus){
         this.sampleCalculateStatus = sampleCalculateStatus;
     }

     public String getSampleCalculateStatus(){
         return sampleCalculateStatus;
     }

     public void setSampleModified(String sampleModified){
         this.sampleModified = sampleModified;
     }

     public String getSampleModified(){
         return sampleModified;
     }

     }
