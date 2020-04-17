package com.hand.hqm.hqm_fmea_staff.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hand.hap.system.dto.BaseDTO;
@ExtensionAttribute(disable=true)
@Table(name = "HQM_FMEA_STAFF")
public class FmeaStaff extends BaseDTO {

     public static final String FIELD_FMEA_ID = "fmeaId";
     public static final String FIELD_STAFF_ID = "staffId";
     public static final String FIELD_KID = "kid";


     private Float fmeaId;

     private Float staffId;

     @Id
     @GeneratedValue
     private Float kid;

     @Transient
     private String userName;
     
     @Transient
     private String employeeCode;
     
     public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public Float getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Float employeeId) {
		this.employeeId = employeeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Transient
     private Float employeeId;
     
     @Transient
     private String name;

     public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setFmeaId(Float fmeaId){
         this.fmeaId = fmeaId;
     }

     public Float getFmeaId(){
         return fmeaId;
     }

     public void setStaffId(Float staffId){
         this.staffId = staffId;
     }

     public Float getStaffId(){
         return staffId;
     }

     public void setKid(Float kid){
         this.kid = kid;
     }

     public Float getKid(){
         return kid;
     }

     }
