package com.hand.hcm.hcm_standard_op.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import com.hand.hap.system.dto.BaseDTO;
import com.hand.hap.core.annotation.MultiLanguageField;
import com.hand.hap.core.annotation.MultiLanguage;


@MultiLanguage
@ExtensionAttribute(disable=true)
@Table(name = "HCM_STANDARD_OP_B")
public class StandardOp extends BaseDTO {

     
     @Id
     @GeneratedValue
     private Float standardOpId;

     private Float plantId;

     @Length(max = 100)
     private String code;

     @Length(max = 1)
     private String keyOpFlag;

     @Length(max = 10)
     private String moveType;

     @Length(max = 10)
     private String inspectType;

     @Length(max = 10)
     private String chargeType;

     @Length(max = 1)
     private String enableFlag;

     private Float processTime;

     private Float standardWorkTime;

     @Length(max = 300)
     private String operationDocument;

     @Length(max = 300)
     private String processProgram;

     private Float cid;

     @Length(max = 100)
     private String prodLine;

     private Float departmentId;

     @Length(max = 20)
     private String version;

     @Length(max = 1)
     private String specialProcessFlag;

     @Length(max = 1)
     private String iqFlag;

     private String description;
     
     private String language;

     public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void setStandardOpId(Float standardOpId){
         this.standardOpId = standardOpId;
     }

     public Float getStandardOpId(){
         return standardOpId;
     }

     public void setPlantId(Float plantId){
         this.plantId = plantId;
     }

     public Float getPlantId(){
         return plantId;
     }

     public void setCode(String code){
         this.code = code;
     }

     public String getCode(){
         return code;
     }

     public void setKeyOpFlag(String keyOpFlag){
         this.keyOpFlag = keyOpFlag;
     }

     public String getKeyOpFlag(){
         return keyOpFlag;
     }

     public void setMoveType(String moveType){
         this.moveType = moveType;
     }

     public String getMoveType(){
         return moveType;
     }

     public void setInspectType(String inspectType){
         this.inspectType = inspectType;
     }

     public String getInspectType(){
         return inspectType;
     }

     public void setChargeType(String chargeType){
         this.chargeType = chargeType;
     }

     public String getChargeType(){
         return chargeType;
     }

     public void setEnableFlag(String enableFlag){
         this.enableFlag = enableFlag;
     }

     public String getEnableFlag(){
         return enableFlag;
     }

     public void setProcessTime(Float processTime){
         this.processTime = processTime;
     }

     public Float getProcessTime(){
         return processTime;
     }

     public void setStandardWorkTime(Float standardWorkTime){
         this.standardWorkTime = standardWorkTime;
     }

     public Float getStandardWorkTime(){
         return standardWorkTime;
     }

     public void setOperationDocument(String operationDocument){
         this.operationDocument = operationDocument;
     }

     public String getOperationDocument(){
         return operationDocument;
     }

     public void setProcessProgram(String processProgram){
         this.processProgram = processProgram;
     }

     public String getProcessProgram(){
         return processProgram;
     }

     public void setCid(Float cid){
         this.cid = cid;
     }

     public Float getCid(){
         return cid;
     }

     public void setProdLine(String prodLine){
         this.prodLine = prodLine;
     }

     public String getProdLine(){
         return prodLine;
     }

     public void setDepartmentId(Float departmentId){
         this.departmentId = departmentId;
     }

     public Float getDepartmentId(){
         return departmentId;
     }

     public void setVersion(String version){
         this.version = version;
     }

     public String getVersion(){
         return version;
     }

     public void setSpecialProcessFlag(String specialProcessFlag){
         this.specialProcessFlag = specialProcessFlag;
     }

     public String getSpecialProcessFlag(){
         return specialProcessFlag;
     }

     public void setIqFlag(String iqFlag){
         this.iqFlag = iqFlag;
     }

     public String getIqFlag(){
         return iqFlag;
     }

     }
