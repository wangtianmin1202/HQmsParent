package com.hand.itf.itf_container_info.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import com.hand.hap.system.dto.BaseDTO;
import java.util.Date;
@ExtensionAttribute(disable=true)
@Table(name = "ITF_CONTAINER_INFO")
public class ContainerInfo extends BaseDTO {

     public static final String FIELD_CONTAINER_INFO_ID = "containerInfoId";
     public static final String FIELD_BATCH_NUMBER = "batchNumber";
     public static final String FIELD_PROCESS_STATUS = "processStatus";
     public static final String FIELD_MESSAGE = "message";
     public static final String FIELD_PROCESS_TIME = "processTime";
     public static final String FIELD_CONTAINER_ID = "containerId";
     public static final String FIELD_CLEAN_FLAG = "cleanFlag";
     public static final String FIELD_REMARK1 = "remark1";
     public static final String FIELD_REMARK2 = "remark2";
     public static final String FIELD_REMARK3 = "remark3";
     public static final String FIELD_REMARK4 = "remark4";
     public static final String FIELD_REMARK5 = "remark5";


     @Id
     @GeneratedValue
     private Float containerInfoId;

     @Length(max = 100)
     private String batchNumber;

     @Length(max = 1)
     private String processStatus;

     @Length(max = 500)
     private String message;

     private Date processTime;

     @Length(max = 100)
     private String containerId;

     @Length(max = 1)
     private String cleanFlag;

     @Length(max = 100)
     private String remark1;

     @Length(max = 100)
     private String remark2;

     @Length(max = 100)
     private String remark3;

     @Length(max = 100)
     private String remark4;

     @Length(max = 100)
     private String remark5;


     public void setContainerInfoId(Float containerInfoId){
         this.containerInfoId = containerInfoId;
     }

     public Float getContainerInfoId(){
         return containerInfoId;
     }

     public void setBatchNumber(String batchNumber){
         this.batchNumber = batchNumber;
     }

     public String getBatchNumber(){
         return batchNumber;
     }

     public void setProcessStatus(String processStatus){
         this.processStatus = processStatus;
     }

     public String getProcessStatus(){
         return processStatus;
     }

     public void setMessage(String message){
         this.message = message;
     }

     public String getMessage(){
         return message;
     }

     public void setProcessTime(Date processTime){
         this.processTime = processTime;
     }

     public Date getProcessTime(){
         return processTime;
     }

     public void setContainerId(String containerId){
         this.containerId = containerId;
     }

     public String getContainerId(){
         return containerId;
     }

     public void setCleanFlag(String cleanFlag){
         this.cleanFlag = cleanFlag;
     }

     public String getCleanFlag(){
         return cleanFlag;
     }

     public void setRemark1(String remark1){
         this.remark1 = remark1;
     }

     public String getRemark1(){
         return remark1;
     }

     public void setRemark2(String remark2){
         this.remark2 = remark2;
     }

     public String getRemark2(){
         return remark2;
     }

     public void setRemark3(String remark3){
         this.remark3 = remark3;
     }

     public String getRemark3(){
         return remark3;
     }

     public void setRemark4(String remark4){
         this.remark4 = remark4;
     }

     public String getRemark4(){
         return remark4;
     }

     public void setRemark5(String remark5){
         this.remark5 = remark5;
     }

     public String getRemark5(){
         return remark5;
     }

     }
