package com.hand.hqm.hqm_mes_ng_msg.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import com.hand.hap.system.dto.BaseDTO;
@ExtensionAttribute(disable=true)
@Table(name = "HQM_MES_NG_MSG")
public class MesNgMsg extends BaseDTO {

     public static final String FIELD_KID = "kid";
     public static final String FIELD_ORDER_NUM = "orderNum";
     public static final String FIELD_PROD_LINE_ID = "prodLineId";
     public static final String FIELD_WORKSTATION_ID = "workstationId";
     public static final String FIELD_ITEM_ID = "itemId";
     public static final String FIELD_SERIAL_NUMER = "serialNumer";
     public static final String FIELD_NG_CODE = "ngCode";
     public static final String FIELD_QTY = "qty";


     @Id
     @GeneratedValue
     private Float kid;

     @Length(max = 50)
     private String orderNum;

     private Float prodLineId;

     private Float workstationId;

     private Float itemId;

     @Length(max = 50)
     private String serialNumer;

     @Length(max = 50)
     private String ngCode;

     private Float qty;


     public void setKid(Float kid){
         this.kid = kid;
     }

     public Float getKid(){
         return kid;
     }

     public void setOrderNum(String orderNum){
         this.orderNum = orderNum;
     }

     public String getOrderNum(){
         return orderNum;
     }

     public void setProdLineId(Float prodLineId){
         this.prodLineId = prodLineId;
     }

     public Float getProdLineId(){
         return prodLineId;
     }

     public void setWorkstationId(Float workstationId){
         this.workstationId = workstationId;
     }

     public Float getWorkstationId(){
         return workstationId;
     }

     public void setItemId(Float itemId){
         this.itemId = itemId;
     }

     public Float getItemId(){
         return itemId;
     }

     public void setSerialNumer(String serialNumer){
         this.serialNumer = serialNumer;
     }

     public String getSerialNumer(){
         return serialNumer;
     }

     public void setNgCode(String ngCode){
         this.ngCode = ngCode;
     }

     public String getNgCode(){
         return ngCode;
     }

     public void setQty(Float qty){
         this.qty = qty;
     }

     public Float getQty(){
         return qty;
     }

     }
