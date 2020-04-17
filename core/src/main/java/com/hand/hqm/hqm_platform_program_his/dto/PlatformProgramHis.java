package com.hand.hqm.hqm_platform_program_his.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import com.hand.hap.system.dto.BaseDTO;
import java.util.Date;
@ExtensionAttribute(disable=true)
@Table(name = "HQM_PLATFORM_PROGRAM_HIS")
public class PlatformProgramHis extends BaseDTO {
	
     @Id
     @GeneratedValue
     private Float eventId;

     private Long eventBy;

     private Date eventTime;

     private Float platformId;

     private Float plantId;

     @Length(max = 30)
     private String programType;

     @Length(max = 30)
     private String platformType;

     @Length(max = 30)
     private String sampleType;

     @Length(max = 1)
     private String enableFlag;


     public void setEventId(Float eventId){
         this.eventId = eventId;
     }

     public Float getEventId(){
         return eventId;
     }

     public void setEventBy(Long eventBy){
         this.eventBy = eventBy;
     }

     public Long getEventBy(){
         return eventBy;
     }

     public void setEventTime(Date eventTime){
         this.eventTime = eventTime;
     }

     public Date getEventTime(){
         return eventTime;
     }

     public void setPlatformId(Float platformId){
         this.platformId = platformId;
     }

     public Float getPlatformId(){
         return platformId;
     }

     public void setPlantId(Float plantId){
         this.plantId = plantId;
     }

     public Float getPlantId(){
         return plantId;
     }

     public void setProgramType(String programType){
         this.programType = programType;
     }

     public String getProgramType(){
         return programType;
     }

     public void setPlatformType(String platformType){
         this.platformType = platformType;
     }

     public String getPlatformType(){
         return platformType;
     }

     public void setSampleType(String sampleType){
         this.sampleType = sampleType;
     }

     public String getSampleType(){
         return sampleType;
     }

     public void setEnableFlag(String enableFlag){
         this.enableFlag = enableFlag;
     }

     public String getEnableFlag(){
         return enableFlag;
     }

     }
