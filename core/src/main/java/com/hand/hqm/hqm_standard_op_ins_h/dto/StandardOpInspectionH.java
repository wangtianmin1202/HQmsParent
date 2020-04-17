package com.hand.hqm.hqm_standard_op_ins_h.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hand.hap.system.dto.BaseDTO;
@ExtensionAttribute(disable=true)
@Table(name = "HQM_STANDARD_OP_INSPECTION_H")
public class StandardOpInspectionH extends BaseDTO {

     public static final String FIELD_HEAD_ID = "headId";
     public static final String FIELD_PLANT_ID = "plantId";
     public static final String FIELD_PROD_LINE_ID = "prodLineId";
     public static final String FIELD_STANDARD_OP_ID = "standardOpId";
     public static final String FIELD_WORKSTATION_ID = "workstationId";
     public static final String FIELD_VERSION = "version";
     public static final String FIELD_ENABLE_FLAG = "enableFlag";


     @Id
     @GeneratedValue
     private Float headId;

     private Float plantId;

     private Float prodLineId;

     private Float standardOpId;

     private Float workstationId;

     @Length(max = 20)
     private String version;

     @Length(max = 1)
     private String enableFlag;
     
     @Transient
     private String plantCode;
     @Transient
     private String prodLineCode;
     @Transient
     private String standardOpCode;
     @Transient   
     private String standardOpDes;     
     @Transient
     private String workstationCode;
     
     private String sourceType;
     @Transient
     private String sourceTypeDes;
     
     public String getSourceTypeDes() {
		return sourceTypeDes;
	}

	public void setSourceTypeDes(String sourceTypeDes) {
		this.sourceTypeDes = sourceTypeDes;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getProdLineCode() {
		return prodLineCode;
	}

	public void setProdLineCode(String prodLineCode) {
		this.prodLineCode = prodLineCode;
	}

	public String getStandardOpCode() {
		return standardOpCode;
	}

	public void setStandardOpCode(String standardOpCode) {
		this.standardOpCode = standardOpCode;
	}

	public String getStandardOpDes() {
		return standardOpDes;
	}

	public void setStandardOpDes(String standardOpDes) {
		this.standardOpDes = standardOpDes;
	}

	public String getWorkstationCode() {
		return workstationCode;
	}

	public void setWorkstationCode(String workstationCode) {
		this.workstationCode = workstationCode;
	}

	


     public String getPlantCode() {
		return plantCode;
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}
	
	

	public void setHeadId(Float headId){
         this.headId = headId;
     }

     public Float getHeadId(){
         return headId;
     }

     public void setPlantId(Float plantId){
         this.plantId = plantId;
     }

     public Float getPlantId(){
         return plantId;
     }

     public void setProdLineId(Float prodLineId){
         this.prodLineId = prodLineId;
     }

     public Float getProdLineId(){
         return prodLineId;
     }

     public void setStandardOpId(Float standardOpId){
         this.standardOpId = standardOpId;
     }

     public Float getStandardOpId(){
         return standardOpId;
     }

     public void setWorkstationId(Float workstationId){
         this.workstationId = workstationId;
     }

     public Float getWorkstationId(){
         return workstationId;
     }

     public void setVersion(String version){
         this.version = version;
     }

     public String getVersion(){
         return version;
     }

     public void setEnableFlag(String enableFlag){
         this.enableFlag = enableFlag;
     }

     public String getEnableFlag(){
         return enableFlag;
     }

     }
