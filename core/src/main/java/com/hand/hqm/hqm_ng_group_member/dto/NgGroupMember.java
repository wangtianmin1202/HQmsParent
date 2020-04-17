package com.hand.hqm.hqm_ng_group_member.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import com.hand.hap.system.dto.BaseDTO;
@ExtensionAttribute(disable=true)
@Table(name = "HQM_NG_GROUP_MEMBER")
public class NgGroupMember extends BaseDTO {

     public static final String FIELD_NG_GROUP_ID = "ngGroupId";
     public static final String FIELD_NG_MENBER_ID = "ngMenberId";
     public static final String FIELD_NG_CODE = "ngCode";
     public static final String FIELD_NG_REASON = "ngReason";
     public static final String FIELD_NG_APPEARANCE = "ngAppearance";
     public static final String FIELD_ENABLE_FLAG = "enableFlag";
     public static final String FIELD_GLOBAL_ATTRIBUTE1 = "globalAttribute1";
     public static final String FIELD_GLOBAL_ATTRIBUTE2 = "globalAttribute2";
     public static final String FIELD_GLOBAL_ATTRIBUTE3 = "globalAttribute3";
     public static final String FIELD_GLOBAL_ATTRIBUTE4 = "globalAttribute4";
     public static final String FIELD_GLOBAL_ATTRIBUTE5 = "globalAttribute5";
     public static final String FIELD_GLOBAL_ATTRIBUTE6 = "globalAttribute6";
     public static final String FIELD_GLOBAL_ATTRIBUTE7 = "globalAttribute7";
     public static final String FIELD_GLOBAL_ATTRIBUTE8 = "globalAttribute8";
     public static final String FIELD_GLOBAL_ATTRIBUTE9 = "globalAttribute9";
     public static final String FIELD_GLOBAL_ATTRIBUTE10 = "globalAttribute10";


     private Float ngGroupId;

     @Id
     @GeneratedValue
     private Float ngMenberId;

     @Length(max = 30)
     private String ngCode;

     @Length(max = 300)
     private String ngReason;

     @Length(max = 30)
     private String ngAppearance;

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


     public void setNgGroupId(Float ngGroupId){
         this.ngGroupId = ngGroupId;
     }

     public Float getNgGroupId(){
         return ngGroupId;
     }

     public void setNgMenberId(Float ngMenberId){
         this.ngMenberId = ngMenberId;
     }

     public Float getNgMenberId(){
         return ngMenberId;
     }

     public void setNgCode(String ngCode){
         this.ngCode = ngCode;
     }

     public String getNgCode(){
         return ngCode;
     }

     public void setNgReason(String ngReason){
         this.ngReason = ngReason;
     }

     public String getNgReason(){
         return ngReason;
     }

     public void setNgAppearance(String ngAppearance){
         this.ngAppearance = ngAppearance;
     }

     public String getNgAppearance(){
         return ngAppearance;
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

     }
