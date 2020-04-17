package com.hand.hqm.hqm_pqc_calendar_c.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import com.hand.hap.system.dto.BaseDTO;
@ExtensionAttribute(disable=true)
@Table(name = "HQM_PQC_CALENDAR_C")
public class PqcCalendarC extends BaseDTO {

     private Float calendarId;

     @Id
     @GeneratedValue
     private Float calendarCId;

     private Float pwaPerson;

     @Length(max = 10)
     private String pwaStatus;


     public void setCalendarId(Float calendarId){
         this.calendarId = calendarId;
     }

     public Float getCalendarId(){
         return calendarId;
     }

     public void setCalendarCId(Float calendarCId){
         this.calendarCId = calendarCId;
     }

     public Float getCalendarCId(){
         return calendarCId;
     }

     public void setPwaPerson(Float pwaPerson){
         this.pwaPerson = pwaPerson;
     }

     public Float getPwaPerson(){
         return pwaPerson;
     }

     public void setPwaStatus(String pwaStatus){
         this.pwaStatus = pwaStatus;
     }

     public String getPwaStatus(){
         return pwaStatus;
     }

     }