package com.hand.hqm.hqm_pqc_inspection_c.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hand.hap.system.dto.BaseDTO;

@ExtensionAttribute(disable = false)
@Table(name = "HQM_PQC_INSPECTION_C")
public class PqcInspectionC extends BaseDTO {

     @Id
     @GeneratedValue
     private Float connectId;

     private Float attributeId;
     
     private String attributeInspectionRes;
     
     private String attributeType;
     
     private Float lineId;

     private Float sampleQty;

     @Length(max = 30)
     private String sizeCodeLetter;

     @Length(max = 30)
     private String ac;

     @Length(max = 30)
     private String re;

     
	@Length(max = 200)
     private String textStandrad;

     private String standradFrom;

     private String standradTo;

     @Length(max = 30)
     private String standradUom;

     private Float conformingQty;

     private Float nonConformingQty;

     @Length(max = 200)
     private String remark;
     
     @Transient
 	 private String inspectionHistory;
     
     @Transient
 	 private String inspectionAttribute;
     
     @Transient
 	 private String standardType;
     
     @Transient
 	 private Float headerId;
     
     @Transient
 	 private String lineListString; //_D表数据的数组JSON字符串
    

	public String getLineListString() {
		return lineListString;
	}

	public void setLineListString(String lineListString) {
		this.lineListString = lineListString;
	}

	public Float getHeaderId() {
		return headerId;
	}

	public void setHeaderId(Float headerId) {
		this.headerId = headerId;
	}

	@Transient
 	 private String fillInType;
     
     @Transient
 	 private String inspectionTool;
     
     @Transient
 	 private String inspectionAttributeType;
     
     private Float precision;
     
     private Float sampleWayId;
     
     private Float sampleType;
     
     private Float parameter;
     
     private String inspectionMethod;
     
     @Transient
     private String sampleProcedureType;
     
     public String getSampleProcedureType() {
		return sampleProcedureType;
	}

	public void setSampleProcedureType(String sampleProcedureType) {
		this.sampleProcedureType = sampleProcedureType;
	}

	public Float getPrecision() {
		return precision;
	}

	public void setPrecision(Float precision) {
		this.precision = precision;
	}

	public Float getSampleWayId() {
		return sampleWayId;
	}

	public void setSampleWayId(Float sampleWayId) {
		this.sampleWayId = sampleWayId;
	}

	public Float getSampleType() {
		return sampleType;
	}

	public void setSampleType(Float sampleType) {
		this.sampleType = sampleType;
	}

	public Float getParameter() {
		return parameter;
	}

	public void setParameter(Float parameter) {
		this.parameter = parameter;
	}

	public String getInspectionMethod() {
		return inspectionMethod;
	}

	public void setInspectionMethod(String inspectionMethod) {
		this.inspectionMethod = inspectionMethod;
	}

	public String getInspectionAttributeType() {
		return inspectionAttributeType;
	}

	public void setInspectionAttributeType(String inspectionAttributeType) {
		this.inspectionAttributeType = inspectionAttributeType;
	}

	public String getInspectionAttribute() {
		return inspectionAttribute;
	}

	public void setInspectionAttribute(String inspectionAttribute) {
		this.inspectionAttribute = inspectionAttribute;
	}

	public String getStandardType() {
		return standardType;
	}

	public void setStandardType(String standardType) {
		this.standardType = standardType;
	}

	public String getFillInType() {
		return fillInType;
	}

	public void setFillInType(String fillInType) {
		this.fillInType = fillInType;
	}

	public String getInspectionTool() {
		return inspectionTool;
	}

	public void setInspectionTool(String inspectionTool) {
		this.inspectionTool = inspectionTool;
	}

	public String getInspectionHistory() {
		return inspectionHistory;
	}

	public void setInspectionHistory(String inspectionHistory) {
		this.inspectionHistory = inspectionHistory;
	}

	public Float getAttributeId() {
 		return attributeId;
 	}

 	public void setAttributeId(Float attributeId) {
 		this.attributeId = attributeId;
 	}

 	public String getAttributeInspectionRes() {
 		return attributeInspectionRes;
 	}

 	public void setAttributeInspectionRes(String attributeInspectionRes) {
 		this.attributeInspectionRes = attributeInspectionRes;
 	}

 	public String getAttributeType() {
 		return attributeType;
 	}

 	public void setAttributeType(String attributeType) {
 		this.attributeType = attributeType;
 	}

     public void setConnectId(Float connectId){
         this.connectId = connectId;
     }

     public Float getConnectId(){
         return connectId;
     }

     public void setLineId(Float lineId){
         this.lineId = lineId;
     }

     public Float getLineId(){
         return lineId;
     }

     public void setSampleQty(Float sampleQty){
         this.sampleQty = sampleQty;
     }

     public Float getSampleQty(){
         return sampleQty;
     }

     public void setSizeCodeLetter(String sizeCodeLetter){
         this.sizeCodeLetter = sizeCodeLetter;
     }

     public String getSizeCodeLetter(){
         return sizeCodeLetter;
     }

     public void setAc(String ac){
         this.ac = ac;
     }

     public String getAc(){
         return ac;
     }

     public void setRe(String re){
         this.re = re;
     }

     public String getRe(){
         return re;
     }

     public void setTextStandrad(String textStandrad){
         this.textStandrad = textStandrad;
     }

     public String getTextStandrad(){
         return textStandrad;
     }

     public void setStandradFrom(String standradFrom){
         this.standradFrom = standradFrom;
     }

     public String getStandradFrom(){
         return standradFrom;
     }

     public void setStandradTo(String standradTo){
         this.standradTo = standradTo;
     }

     public String getStandradTo(){
         return standradTo;
     }

     public void setStandradUom(String standradUom){
         this.standradUom = standradUom;
     }

     public String getStandradUom(){
         return standradUom;
     }

     public void setConformingQty(Float conformingQty){
         this.conformingQty = conformingQty;
     }

     public Float getConformingQty(){
         return conformingQty;
     }

     public void setNonConformingQty(Float nonConformingQty){
         this.nonConformingQty = nonConformingQty;
     }

     public Float getNonConformingQty(){
         return nonConformingQty;
     }

     public void setRemark(String remark){
         this.remark = remark;
     }

     public String getRemark(){
         return remark;
     }

     }
