package com.hand.sys.sys_individuation_query.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import com.hand.hap.system.dto.BaseDTO;
@ExtensionAttribute(disable=true)
@Table(name = "SYS_INDIVIDUATION_QUERY")
public class IndividuationQuery extends BaseDTO {

     public static final String FIELD_INDIVIDUATION_ID = "individuationId";
     public static final String FIELD_COLUMN_CODE = "columnCode";
     public static final String FIELD_COLUMN_DESC = "columnDesc";
     public static final String FIELD_COLUMN_TYPE = "columnType";
     public static final String FIELD_COLUMN_TYPE_CODE = "columnTypeCode";
     public static final String FIELD_COLUMN_VERIFY = "columnVerify";
     public static final String FIELD_FUNCTION_CODE = "functionCode";


     @Id
     @GeneratedValue
     private Float individuationId;

     @Length(max = 50)
     private String columnCode;

     @Length(max = 150)
     private String columnDesc;

     @Length(max = 50)
     private String columnType;

     @Length(max = 50)
     private String columnTypeCode;

     @Length(max = 150)
     private String columnVerify;

     @Length(max = 50)
     private String functionCode;


     public void setIndividuationId(Float individuationId){
         this.individuationId = individuationId;
     }

     public Float getIndividuationId(){
         return individuationId;
     }

     public void setColumnCode(String columnCode){
         this.columnCode = columnCode;
     }

     public String getColumnCode(){
         return columnCode;
     }

     public void setColumnDesc(String columnDesc){
         this.columnDesc = columnDesc;
     }

     public String getColumnDesc(){
         return columnDesc;
     }

     public void setColumnType(String columnType){
         this.columnType = columnType;
     }

     public String getColumnType(){
         return columnType;
     }

     public void setColumnTypeCode(String columnTypeCode){
         this.columnTypeCode = columnTypeCode;
     }

     public String getColumnTypeCode(){
         return columnTypeCode;
     }

     public void setColumnVerify(String columnVerify){
         this.columnVerify = columnVerify;
     }

     public String getColumnVerify(){
         return columnVerify;
     }

     public void setFunctionCode(String functionCode){
         this.functionCode = functionCode;
     }

     public String getFunctionCode(){
         return functionCode;
     }

     }