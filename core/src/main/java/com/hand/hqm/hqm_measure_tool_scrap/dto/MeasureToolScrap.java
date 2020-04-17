package com.hand.hqm.hqm_measure_tool_scrap.dto;

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
@Table(name = "HQM_MEASURE_TOOL_SCRAP")
public class MeasureToolScrap extends BaseDTO {

     public static final String FIELD_MEASURE_TOOL_SCRAP_ID = "measureToolScrapId";
     public static final String FIELD_MEASURE_TOOL_ID = "measureToolId";
     public static final String FIELD_SCRAP_NUMBER = "scrapNumber";
     public static final String FIELD_APPLICATION_DATE = "applicationDate";
     public static final String FIELD_END_DATE = "endDate";
     public static final String FIELD_REASON = "reason";
     public static final String FIELD_BUSINESS_KEY = "businessKey";
     public static final String FIELD_PROCESS_START_TIME = "processStartTime";
     public static final String FIELD_PROCESS_END_TIME = "processEndTime";
     public static final String FIELD_PROCESS_INSTANCE_ID = "processInstanceId";
     public static final String FIELD_PROCESS_STATUS = "processStatus";
     public static final String FIELD_STATUS = "status";


     @Id
     @GeneratedValue
     private Float measureToolScrapId;

     private Float measureToolId;

     @Length(max = 64)
     private String scrapNumber;

     private Date applicationDate;

     private Date endDate;

     @Length(max = 64)
     private String reason;

     @Length(max = 64)
     private String businessKey;

     private Date processStartTime;

     private Date processEndTime;

     @Length(max = 64)
     private String processInstanceId;

     @Length(max = 64)
     private String processStatus;

     @Length(max = 64)
     private String status;

     @Transient
     private String measureToolNum;//设备编号
     
     @Transient
     private String descriptions;//设备名称
     
     @Transient
     private String measureToolType;//型号
     
     @Transient
     private String measuringRange;//规格-测量范围
     
     @Transient
     private String taskId;//当前节点任务
     
     @Transient
     private Long createdBy;//订单创建人

     public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getMeasureToolNum() {
		return measureToolNum;
	}

	public void setMeasureToolNum(String measureToolNum) {
		this.measureToolNum = measureToolNum;
	}

	public String getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}

	public String getMeasureToolType() {
		return measureToolType;
	}

	public void setMeasureToolType(String measureToolType) {
		this.measureToolType = measureToolType;
	}

	public String getMeasuringRange() {
		return measuringRange;
	}

	public void setMeasuringRange(String measuringRange) {
		this.measuringRange = measuringRange;
	}

	public void setMeasureToolScrapId(Float measureToolScrapId){
         this.measureToolScrapId = measureToolScrapId;
     }

     public Float getMeasureToolScrapId(){
         return measureToolScrapId;
     }

     public void setMeasureToolId(Float measureToolId){
         this.measureToolId = measureToolId;
     }

     public Float getMeasureToolId(){
         return measureToolId;
     }

     public void setScrapNumber(String scrapNumber){
         this.scrapNumber = scrapNumber;
     }

     public String getScrapNumber(){
         return scrapNumber;
     }

     public void setApplicationDate(Date applicationDate){
         this.applicationDate = applicationDate;
     }

     public Date getApplicationDate(){
         return applicationDate;
     }

     public void setEndDate(Date endDate){
         this.endDate = endDate;
     }

     public Date getEndDate(){
         return endDate;
     }

     public void setReason(String reason){
         this.reason = reason;
     }

     public String getReason(){
         return reason;
     }

     public void setBusinessKey(String businessKey){
         this.businessKey = businessKey;
     }

     public String getBusinessKey(){
         return businessKey;
     }

     public void setProcessStartTime(Date processStartTime){
         this.processStartTime = processStartTime;
     }

     public Date getProcessStartTime(){
         return processStartTime;
     }

     public void setProcessEndTime(Date processEndTime){
         this.processEndTime = processEndTime;
     }

     public Date getProcessEndTime(){
         return processEndTime;
     }

     public void setProcessInstanceId(String processInstanceId){
         this.processInstanceId = processInstanceId;
     }

     public String getProcessInstanceId(){
         return processInstanceId;
     }

     public void setProcessStatus(String processStatus){
         this.processStatus = processStatus;
     }

     public String getProcessStatus(){
         return processStatus;
     }

     public void setStatus(String status){
         this.status = status;
     }

     public String getStatus(){
         return status;
     }

     }
