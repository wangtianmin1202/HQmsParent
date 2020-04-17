package com.hand.hqm.hqm_fqc_sample_switch.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import com.hand.hap.system.dto.BaseDTO;
@ExtensionAttribute(disable=true)
@Table(name = "HQM_FQC_SAMPLE_SWITCH")
public class FqcSampleSwitch extends BaseDTO {

     public static final String FIELD_SWITCH_ID = "switchId";
     public static final String FIELD_SOURCE_SAMPLE_TYPE = "sourceSampleType";
     public static final String FIELD_NEW_SAMPLE_TYPE = "newSampleType";
     public static final String FIELD_SWITCH_RULE_VALUE_N = "switchRuleValueN";
     public static final String FIELD_SWITCH_RULE_VALUE_M = "switchRuleValueM";
     public static final String FIELD_ENABLE_FLAG = "enableFlag";
     public static final String FIELD_INSPECTION_JUDGE = "inspectionJudge";


     @Id
     @GeneratedValue
     private Float switchId;

     @Length(max = 50)
     private String sourceSampleType;

     @Length(max = 50)
     private String newSampleType;

     private Float switchRuleValueN;

     @Length(max = 30)
     private String switchRuleValueM;

     private String enableFlag;

     @Length(max = 50)
     private String inspectionJudge;

     private String sampleProcedureType;

     public String getSampleProcedureType() {
		return sampleProcedureType;
	}

	public void setSampleProcedureType(String sampleProcedureType) {
		this.sampleProcedureType = sampleProcedureType;
	}

	public void setSwitchId(Float switchId){
         this.switchId = switchId;
     }

     public Float getSwitchId(){
         return switchId;
     }

     public void setSourceSampleType(String sourceSampleType){
         this.sourceSampleType = sourceSampleType;
     }

     public String getSourceSampleType(){
         return sourceSampleType;
     }

     public void setNewSampleType(String newSampleType){
         this.newSampleType = newSampleType;
     }

     public String getNewSampleType(){
         return newSampleType;
     }

     public void setSwitchRuleValueN(Float switchRuleValueN){
         this.switchRuleValueN = switchRuleValueN;
     }

     public Float getSwitchRuleValueN(){
         return switchRuleValueN;
     }

     public void setSwitchRuleValueM(String switchRuleValueM){
         this.switchRuleValueM = switchRuleValueM;
     }

     public String getSwitchRuleValueM(){
         return switchRuleValueM;
     }

     public void setEnableFlag(String enableFlag){
         this.enableFlag = enableFlag;
     }

     public String getEnableFlag(){
         return enableFlag;
     }

     public void setInspectionJudge(String inspectionJudge){
         this.inspectionJudge = inspectionJudge;
     }

     public String getInspectionJudge(){
         return inspectionJudge;
     }

     }