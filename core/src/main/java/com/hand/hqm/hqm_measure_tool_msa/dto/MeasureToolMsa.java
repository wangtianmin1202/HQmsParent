package com.hand.hqm.hqm_measure_tool_msa.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import com.hand.hap.system.dto.BaseDTO;
@ExtensionAttribute(disable=true)
@Table(name = "HQM_MEASURE_TOOL_MSA")
public class MeasureToolMsa extends BaseDTO {

     public static final String FIELD_MEASURE_TOOL_MSA_ID = "measureToolMsaId";
     public static final String FIELD_MEASURE_TOOL_ID = "measureToolId";
     public static final String FIELD_MSA_CONTENT = "msaContent";
     public static final String FIELD_JUDGEMENT_STANDARD = "judgementStandard";
     public static final String FIELD_MSA_STATUS = "msaStatus";
     public static final String FIELD_MSA_RESULT = "msaResult";
     public static final String FIELD_KEY_PROCESS = "keyProcess";
     public static final String FIELD_POSITION_TITLE = "positionTitle";
     public static final String FIELD_ENABLE_FLAG = "enableFlag";


     @Id
     @GeneratedValue
     private Float measureToolMsaId;

     private Float measureToolId;

     @Length(max = 50)
     private String msaContent;

     @Length(max = 5)
     private String msaResult;

     



     public void setMeasureToolMsaId(Float measureToolMsaId){
         this.measureToolMsaId = measureToolMsaId;
     }

     public Float getMeasureToolMsaId(){
         return measureToolMsaId;
     }

     public void setMeasureToolId(Float measureToolId){
         this.measureToolId = measureToolId;
     }

     public Float getMeasureToolId(){
         return measureToolId;
     }

     public void setMsaContent(String msaContent){
         this.msaContent = msaContent;
     }

     public String getMsaContent(){
         return msaContent;
     }

     public void setMsaResult(String msaResult){
         this.msaResult = msaResult;
     }

     public String getMsaResult(){
         return msaResult;
     }


     }
