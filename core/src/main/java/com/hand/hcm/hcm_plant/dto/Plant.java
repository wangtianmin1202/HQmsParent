package com.hand.hcm.hcm_plant.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hand.hap.system.dto.BaseDTO;
@ExtensionAttribute(disable=true)
@Table(name = "HCM_PLANT")
public class Plant{

     public static final String FIELD_SCHEDULE_REGION_ID = "scheduleRegionId";
     public static final String FIELD_PLANT_ID = "plantId";
     public static final String FIELD_PLANT_CODE = "plantCode";
     public static final String FIELD_DESCRIPTIONS = "descriptions";
     public static final String FIELD_ENABLE_FLAG = "enableFlag";
     public static final String FIELD_MAIN_PLANT_FLAG = "mainPlantFlag";
     public static final String FIELD_IS_DELIVERY_REGION = "isDeliveryRegion";


     private Float scheduleRegionId;

     @Id
     @GeneratedValue
     private Float plantId;

     @Length(max = 30)
     private String plantCode;

     @Length(max = 300)
     private String descriptions;

     @Length(max = 1)
     private String enableFlag;

     @Length(max = 1)
     private String mainPlantFlag;

     @Length(max = 1)
     private String isDeliveryRegion;
     
     @Transient
     private String plantName;//收获组织
     
     @Transient
     private Float supplierId;//供应商id


     public Float getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Float supplierId) {
		this.supplierId = supplierId;
	}

	public void setScheduleRegionId(Float scheduleRegionId){
         this.scheduleRegionId = scheduleRegionId;
     }

     public Float getScheduleRegionId(){
         return scheduleRegionId;
     }

     public void setPlantId(Float plantId){
         this.plantId = plantId;
     }

     public Float getPlantId(){
         return plantId;
     }

     public void setPlantCode(String plantCode){
         this.plantCode = plantCode;
     }

     public String getPlantCode(){
         return plantCode;
     }

     public void setDescriptions(String descriptions){
         this.descriptions = descriptions;
     }

     public String getDescriptions(){
         return descriptions;
     }

     public void setEnableFlag(String enableFlag){
         this.enableFlag = enableFlag;
     }

     public String getEnableFlag(){
         return enableFlag;
     }

     public void setMainPlantFlag(String mainPlantFlag){
         this.mainPlantFlag = mainPlantFlag;
     }

     public String getMainPlantFlag(){
         return mainPlantFlag;
     }

     public void setIsDeliveryRegion(String isDeliveryRegion){
         this.isDeliveryRegion = isDeliveryRegion;
     }

     public String getIsDeliveryRegion(){
         return isDeliveryRegion;
     }

	public String getPlantName() {
		return plantName;
	}

	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}

     }
