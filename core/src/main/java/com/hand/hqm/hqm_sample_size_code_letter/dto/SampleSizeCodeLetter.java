package com.hand.hqm.hqm_sample_size_code_letter.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hand.hap.system.dto.BaseDTO;
import org.hibernate.validator.constraints.NotEmpty;

@ExtensionAttribute(disable=true)
@Table(name = "HQM_SAMPLE_SIZE_CODE_LETTER")
public class SampleSizeCodeLetter extends BaseDTO {

     @Id
     @GeneratedValue
     private Float letterId;

     @Length(max = 30)
     private String sampleProcedureType;

     
     private Float lotSizeFrom;


     private Float lotSizeTo;

     @Length(max = 30)
     private String inspectionLevels;
     
    @Transient
    private String value;
    @Transient
    private String meaning;
    @Transient
    @NotEmpty
     private String inspectionLevelsOne;
    @Transient
    @NotEmpty
    private String inspectionLevelsTwo;
    

	@Transient
    @NotEmpty
    private String inspectionLevelsThree;
    @Transient
    @NotEmpty
    private String inspectionLevelsFour;
    @Transient
    @NotEmpty
    private String inspectionLevelsFive;
    @Transient
    @NotEmpty
    private String inspectionLevelsSix;
    @Transient
    @NotEmpty
    private String inspectionLevelsSeven;
     @Length(max = 30)
     private String sizeCodeLetter;

     @Length(max = 1)
     private String enableFlag;

     @Length(max = 150)
     private String globalAttribute1;

     @Length(max = 150)
     private String globalAttribute2;

     @Length(max = 150)
     private String globalAttribute3;

     @Length(max = 150)
     private String globalAttribute4;

     @Length(max = 150)
     private String globalAttribute5;

     @Length(max = 150)
     private String globalAttribute6;

     @Length(max = 150)
     private String globalAttribute7;

     @Length(max = 150)
     private String globalAttribute8;

     @Length(max = 150)
     private String globalAttribute9;

     @Length(max = 150)
     private String globalAttribute10;

     @Length(max = 150)
     private String globalAttribute11;

     @Length(max = 150)
     private String globalAttribute12;

     @Length(max = 150)
     private String globalAttribute13;

     @Length(max = 150)
     private String globalAttribute14;

     @Length(max = 150)
     private String globalAttribute15;
     public String getValue() {
 		return value;
 	}

 	public void setValue(String value) {
 		this.value = value;
 	}

 	public String getMeaning() {
 		return meaning;
 	}

 	public void setMeaning(String meaning) {
 		this.meaning = meaning;
 	}
    public String getInspectionLevelsOne() {
        return inspectionLevelsOne;
    }

    public void setInspectionLevelsOne(String inspectionLevelsOne) {
        this.inspectionLevelsOne = inspectionLevelsOne;
    }

    public String getInspectionLevelsTwo() {
        return inspectionLevelsTwo;
    }

    public void setInspectionLevelsTwo(String inspectionLevelsTwo) {
        this.inspectionLevelsTwo = inspectionLevelsTwo;
    }

    public String getInspectionLevelsThree() {
        return inspectionLevelsThree;
    }

    public void setInspectionLevelsThree(String inspectionLevelsThree) {
        this.inspectionLevelsThree = inspectionLevelsThree;
    }

    public String getInspectionLevelsFour() {
        return inspectionLevelsFour;
    }

    public void setInspectionLevelsFour(String inspectionLevelsFour) {
        this.inspectionLevelsFour = inspectionLevelsFour;
    }

    public String getInspectionLevelsFive() {
        return inspectionLevelsFive;
    }

    public void setInspectionLevelsFive(String inspectionLevelsFive) {
        this.inspectionLevelsFive = inspectionLevelsFive;
    }

    public String getInspectionLevelsSix() {
        return inspectionLevelsSix;
    }

    public void setInspectionLevelsSix(String inspectionLevelsSix) {
        this.inspectionLevelsSix = inspectionLevelsSix;
    }

    public String getInspectionLevelsSeven() {
        return inspectionLevelsSeven;
    }

    public void setInspectionLevelsSeven(String inspectionLevelsSeven) {
        this.inspectionLevelsSeven = inspectionLevelsSeven;
    }

     public void setLetterId(Float letterId){
         this.letterId = letterId;
     }

     public Float getLetterId(){
         return letterId;
     }

     public void setSampleProcedureType(String sampleProcedureType){
         this.sampleProcedureType = sampleProcedureType;
     }

     public String getSampleProcedureType(){
         return sampleProcedureType;
     }

     public void setLotSizeFrom(Float lotSizeFrom){
         this.lotSizeFrom = lotSizeFrom;
     }

     public Float getLotSizeFrom(){
         return lotSizeFrom;
     }

     public void setLotSizeTo(Float lotSizeTo){
         this.lotSizeTo = lotSizeTo;
     }

     public Float getLotSizeTo(){
         return lotSizeTo;
     }

     public void setInspectionLevels(String inspectionLevels){
         this.inspectionLevels = inspectionLevels;
     }

     public String getInspectionLevels(){
         return inspectionLevels;
     }

     public void setSizeCodeLetter(String sizeCodeLetter){
         this.sizeCodeLetter = sizeCodeLetter;
     }

     public String getSizeCodeLetter(){
         return sizeCodeLetter;
     }

     public void setEnableFlag(String enableFlag){
         this.enableFlag = enableFlag;
     }

     public String getEnableFlag(){
         return enableFlag;
     }

     public void setGlobalAttribute1(String globalAttribute1){
         this.globalAttribute1 = globalAttribute1;
     }

     public String getGlobalAttribute1(){
         return globalAttribute1;
     }

     public void setGlobalAttribute2(String globalAttribute2){
         this.globalAttribute2 = globalAttribute2;
     }

     public String getGlobalAttribute2(){
         return globalAttribute2;
     }

     public void setGlobalAttribute3(String globalAttribute3){
         this.globalAttribute3 = globalAttribute3;
     }

     public String getGlobalAttribute3(){
         return globalAttribute3;
     }

     public void setGlobalAttribute4(String globalAttribute4){
         this.globalAttribute4 = globalAttribute4;
     }

     public String getGlobalAttribute4(){
         return globalAttribute4;
     }

     public void setGlobalAttribute5(String globalAttribute5){
         this.globalAttribute5 = globalAttribute5;
     }

     public String getGlobalAttribute5(){
         return globalAttribute5;
     }

     public void setGlobalAttribute6(String globalAttribute6){
         this.globalAttribute6 = globalAttribute6;
     }

     public String getGlobalAttribute6(){
         return globalAttribute6;
     }

     public void setGlobalAttribute7(String globalAttribute7){
         this.globalAttribute7 = globalAttribute7;
     }

     public String getGlobalAttribute7(){
         return globalAttribute7;
     }

     public void setGlobalAttribute8(String globalAttribute8){
         this.globalAttribute8 = globalAttribute8;
     }

     public String getGlobalAttribute8(){
         return globalAttribute8;
     }

     public void setGlobalAttribute9(String globalAttribute9){
         this.globalAttribute9 = globalAttribute9;
     }

     public String getGlobalAttribute9(){
         return globalAttribute9;
     }

     public void setGlobalAttribute10(String globalAttribute10){
         this.globalAttribute10 = globalAttribute10;
     }

     public String getGlobalAttribute10(){
         return globalAttribute10;
     }

     public void setGlobalAttribute11(String globalAttribute11){
         this.globalAttribute11 = globalAttribute11;
     }

     public String getGlobalAttribute11(){
         return globalAttribute11;
     }

     public void setGlobalAttribute12(String globalAttribute12){
         this.globalAttribute12 = globalAttribute12;
     }

     public String getGlobalAttribute12(){
         return globalAttribute12;
     }

     public void setGlobalAttribute13(String globalAttribute13){
         this.globalAttribute13 = globalAttribute13;
     }

     public String getGlobalAttribute13(){
         return globalAttribute13;
     }

     public void setGlobalAttribute14(String globalAttribute14){
         this.globalAttribute14 = globalAttribute14;
     }

     public String getGlobalAttribute14(){
         return globalAttribute14;
     }

     public void setGlobalAttribute15(String globalAttribute15){
         this.globalAttribute15 = globalAttribute15;
     }

     public String getGlobalAttribute15(){
         return globalAttribute15;
     }

     }
