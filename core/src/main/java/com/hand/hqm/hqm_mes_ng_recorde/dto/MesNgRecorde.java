package com.hand.hqm.hqm_mes_ng_recorde.dto;

import java.util.Date;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import com.hand.hap.system.dto.BaseDTO;
@ExtensionAttribute(disable=true)
@Table(name = "HQM_MES_NG_RECORDE")
public class MesNgRecorde extends BaseDTO {

     @Id
     @GeneratedValue
     private Float taskId;

     private Float plantId;

     @Length(max = 50)
     private String taskNumber;

     @Length(max = 50)
     private String taskType;

     @Length(max = 50)
     private String number8d;

     private Float prodLineId;

     private Float workstationsId;

     private Float itemId;

     private Date eventTime;

     public Date getEventTime() {
		return eventTime;
	}

	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}

	public void setTaskId(Float taskId){
         this.taskId = taskId;
     }

     public Float getTaskId(){
         return taskId;
     }

     public void setPlantId(Float plantId){
         this.plantId = plantId;
     }

     public Float getPlantId(){
         return plantId;
     }

     public void setTaskNumber(String taskNumber){
         this.taskNumber = taskNumber;
     }

     public String getTaskNumber(){
         return taskNumber;
     }

     public void setTaskType(String taskType){
         this.taskType = taskType;
     }

     public String getTaskType(){
         return taskType;
     }

     public void setNumber8d(String number8d){
         this.number8d = number8d;
     }

     public String getNumber8d(){
         return number8d;
     }

     public void setProdLineId(Float prodLineId){
         this.prodLineId = prodLineId;
     }

     public Float getProdLineId(){
         return prodLineId;
     }

     public void setWorkstationsId(Float workstationsId){
         this.workstationsId = workstationsId;
     }

     public Float getWorkstationsId(){
         return workstationsId;
     }

     public void setItemId(Float itemId){
         this.itemId = itemId;
     }

     public Float getItemId(){
         return itemId;
     }

     }
