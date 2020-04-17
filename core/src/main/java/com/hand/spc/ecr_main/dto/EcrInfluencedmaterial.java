package com.hand.spc.ecr_main.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import com.hand.hap.system.dto.BaseDTO;
@ExtensionAttribute(disable=true)
@Table(name = "HPM_ECR_INFLUENCEDMATERIAL")
public class EcrInfluencedmaterial extends BaseDTO {

     public static final String FIELD_KID = "kid";
     public static final String FIELD_MATERIAL_ID = "materialId";
     public static final String FIELD_ECRNO = "ecrno";


     @Id
     @GeneratedValue
     private Long kid;

     private Long materialId;

     @Length(max = 80)
     private String ecrno;
     
     private String mainPosition;
     
     private String subPosition;
     
     private String mainDuty;
     
     private String subDuty;
     
     private String categoryFirst;
     
     private String categorySecond;
     
     private String categoryThird;
     
     
     private String itemVersion;	 
     private String wmsOnhand	;
     private String mesOnhand	;
     private String poQty	;
     private String supplierOnhand	;     
     private String specialOnhand;
     private String calculateOnhand;
     private String onhandStatus;	 

     private String buyerCycle;
     
     private Float specialWipOnhand  ;
     
     private String buyer;
     
     
     public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public Float getSpecialWipOnhand() {
		return specialWipOnhand;
	}

	public void setSpecialWipOnhand(Float specialWipOnhand) {
		this.specialWipOnhand = specialWipOnhand;
	}
     
     public String getBuyerCycle() {
		return buyerCycle;
	}

	public void setBuyerCycle(String buyerCycle) {
		this.buyerCycle = buyerCycle;
	}

	public String getItemVersion() {
		return itemVersion;
	}

	public void setItemVersion(String itemVersion) {
		this.itemVersion = itemVersion;
	}

	public String getWmsOnhand() {
		return wmsOnhand;
	}

	public void setWmsOnhand(String wmsOnhand) {
		this.wmsOnhand = wmsOnhand;
	}

	public String getMesOnhand() {
		return mesOnhand;
	}

	public void setMesOnhand(String mesOnhand) {
		this.mesOnhand = mesOnhand;
	}

	public String getPoQty() {
		return poQty;
	}

	public void setPoQty(String poQty) {
		this.poQty = poQty;
	}

	public String getSupplierOnhand() {
		return supplierOnhand;
	}

	public void setSupplierOnhand(String supplierOnhand) {
		this.supplierOnhand = supplierOnhand;
	}

	public String getSpecialOnhand() {
		return specialOnhand;
	}

	public void setSpecialOnhand(String specialOnhand) {
		this.specialOnhand = specialOnhand;
	}

	public String getCalculateOnhand() {
		return calculateOnhand;
	}

	public void setCalculateOnhand(String calculateOnhand) {
		this.calculateOnhand = calculateOnhand;
	}

	public String getOnhandStatus() {
		return onhandStatus;
	}

	public void setOnhandStatus(String onhandStatus) {
		this.onhandStatus = onhandStatus;
	}

	public String getMainPosition() {
		return mainPosition;
	}

	public void setMainPosition(String mainPosition) {
		this.mainPosition = mainPosition;
	}

	public String getSubPosition() {
		return subPosition;
	}

	public void setSubPosition(String subPosition) {
		this.subPosition = subPosition;
	}

	public String getMainDuty() {
		return mainDuty;
	}

	public void setMainDuty(String mainDuty) {
		this.mainDuty = mainDuty;
	}

	public String getSubDuty() {
		return subDuty;
	}

	public void setSubDuty(String subDuty) {
		this.subDuty = subDuty;
	}

	public String getCategoryFirst() {
		return categoryFirst;
	}

	public void setCategoryFirst(String categoryFirst) {
		this.categoryFirst = categoryFirst;
	}

	public String getCategorySecond() {
		return categorySecond;
	}

	public void setCategorySecond(String categorySecond) {
		this.categorySecond = categorySecond;
	}

	public String getCategoryThird() {
		return categoryThird;
	}

	public void setCategoryThird(String categoryThird) {
		this.categoryThird = categoryThird;
	}

	public void setKid(Long kid){
         this.kid = kid;
     }

     public Long getKid(){
         return kid;
     }

     public void setMaterialId(Long long1){
         this.materialId = long1;
     }

     public Long getMaterialId(){
         return materialId;
     }

     public void setEcrno(String ecrno){
         this.ecrno = ecrno;
     }

     public String getEcrno(){
         return ecrno;
     }

     }