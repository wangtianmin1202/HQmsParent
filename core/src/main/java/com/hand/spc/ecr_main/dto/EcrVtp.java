package com.hand.spc.ecr_main.dto;

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
@Table(name = "HPM_ECR_VTP")
public class EcrVtp extends BaseDTO {

     public static final String FIELD_KID = "kid";
     public static final String FIELD_ECRNO = "ecrno";
     public static final String FIELD_SKU_CODE = "skuCode";
     public static final String FIELD_VTP_NUM = "vtpNum";
     public static final String FIELD_VTP_SEQ = "vtpSeq";
     public static final String FIELD_CONCLUTION = "conclution";
     public static final String FIELD_STATUS = "status";
     public static final String FIELD_DUTYBY = "dutyby";
     public static final String FIELD_FINISHED_DATE = "finishedDate";
     public static final String FIELD_PLAN_FINISHED_DATE = "planFinishedDate";
     public static final String FIELD_ACTUALLY_FINISHED_DATE = "actuallyFinishedDate";
     public static final String FIELD_IS_NEED = "isNeed";


     @Id
     @GeneratedValue
     private Long kid;

     @Length(max = 80)
     private String ecrno;

     @Length(max = 30)
     private String skuCode;

     @Length(max = 30)
     private String vtpNum;

     private Long vtpSeq;

     @Length(max = 10)
     private String conclution;

     @Length(max = 10)
     private String status;

     @Length(max = 30)
     private String dutyby;

     private Date finishedDate;

     private Date planFinishedDate;

     private Date actuallyFinishedDate;

     @Length(max = 10)
     private String isNeed;

     @Transient
     private String skuDesc;


     public String getSkuDesc() {
		return skuDesc;
	}

	public void setSkuDesc(String skuDesc) {
		this.skuDesc = skuDesc;
	}

	public void setKid(Long kid){
         this.kid = kid;
     }

     public Long getKid(){
         return kid;
     }

     public void setEcrno(String ecrno){
         this.ecrno = ecrno;
     }

     public String getEcrno(){
         return ecrno;
     }

     public void setSkuCode(String skuCode){
         this.skuCode = skuCode;
     }

     public String getSkuCode(){
         return skuCode;
     }

     public void setVtpNum(String vtpNum){
         this.vtpNum = vtpNum;
     }

     public String getVtpNum(){
         return vtpNum;
     }

     public void setVtpSeq(Long vtpSeq){
         this.vtpSeq = vtpSeq;
     }

     public Long getVtpSeq(){
         return vtpSeq;
     }

     public void setConclution(String conclution){
         this.conclution = conclution;
     }

     public String getConclution(){
         return conclution;
     }

     public void setStatus(String status){
         this.status = status;
     }

     public String getStatus(){
         return status;
     }

     public void setDutyby(String dutyby){
         this.dutyby = dutyby;
     }

     public String getDutyby(){
         return dutyby;
     }

     public void setFinishedDate(Date finishedDate){
         this.finishedDate = finishedDate;
     }

     public Date getFinishedDate(){
         return finishedDate;
     }

     public void setPlanFinishedDate(Date planFinishedDate){
         this.planFinishedDate = planFinishedDate;
     }

     public Date getPlanFinishedDate(){
         return planFinishedDate;
     }

     public void setActuallyFinishedDate(Date actuallyFinishedDate){
         this.actuallyFinishedDate = actuallyFinishedDate;
     }

     public Date getActuallyFinishedDate(){
         return actuallyFinishedDate;
     }

     public void setIsNeed(String isNeed){
         this.isNeed = isNeed;
     }

     public String getIsNeed(){
         return isNeed;
     }

     }
