package com.hand.hqm.hqm_item_type_tests_his.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import com.hand.hap.system.dto.BaseDTO;
import java.util.Date;
@ExtensionAttribute(disable=true)
@Table(name = "HQM_ITEM_TYPE_TESTS_HIS")
public class ItemTypeTestsHis extends BaseDTO {

     @Id
     @GeneratedValue
     private Float eventId;

     private Long eventBy;

     private Date eventTime;

     private Float kid;

     private Float plantId;

     @Length(max = 30)
     private String testType;

     private Float itemId;

     private Float categoryId;

     private Float triggerNum;

     private Float typeChangeTime;

     @Length(max = 1)
     private String enableFlag;

     private Float totalQty;

     private Date lastTime;


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

     public void setKid(Float kid){
         this.kid = kid;
     }

     public Float getKid(){
         return kid;
     }

     public void setPlantId(Float plantId){
         this.plantId = plantId;
     }

     public Float getPlantId(){
         return plantId;
     }

     public void setTestType(String testType){
         this.testType = testType;
     }

     public String getTestType(){
         return testType;
     }

     public void setItemId(Float itemId){
         this.itemId = itemId;
     }

     public Float getItemId(){
         return itemId;
     }

     public void setCategoryId(Float categoryId){
         this.categoryId = categoryId;
     }

     public Float getCategoryId(){
         return categoryId;
     }

     public void setTriggerNum(Float triggerNum){
         this.triggerNum = triggerNum;
     }

     public Float getTriggerNum(){
         return triggerNum;
     }

     public void setTypeChangeTime(Float typeChangeTime){
         this.typeChangeTime = typeChangeTime;
     }

     public Float getTypeChangeTime(){
         return typeChangeTime;
     }

     public void setEnableFlag(String enableFlag){
         this.enableFlag = enableFlag;
     }

     public String getEnableFlag(){
         return enableFlag;
     }

     public void setTotalQty(Float totalQty){
         this.totalQty = totalQty;
     }

     public Float getTotalQty(){
         return totalQty;
     }

     public void setLastTime(Date lastTime){
         this.lastTime = lastTime;
     }

     public Date getLastTime(){
         return lastTime;
     }

     }
