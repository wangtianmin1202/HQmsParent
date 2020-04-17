package com.hand.spc.ecr_main.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import com.hand.hap.system.dto.BaseDTO;
@ExtensionAttribute(disable=true)
@Table(name = "HPM_ITEM_SKU_RELAITION")
public class ItemSkuRelaition extends BaseDTO {

     public static final String FIELD_RELATION_ID = "relationId";
     public static final String FIELD_ITEM_SKU_ID = "itemSkuId";
     public static final String FIELD_ITEM_ECR_ID = "itemEcrId";


     @Id
     @GeneratedValue
     private Long relationId;

     private Long itemSkuId;

     private Long itemEcrId;

     
     private Float bomUse;

     private Float ecrUse;

     @Length(max = 10)
     private String subItemFlag;

     @Length(max = 30)
     private String subItem;

     
     
	public Float getBomUse() {
		return bomUse;
	}

	public void setBomUse(Float bomUse) {
		this.bomUse = bomUse;
	}

	public Float getEcrUse() {
		return ecrUse;
	}

	public void setEcrUse(Float ecrUse) {
		this.ecrUse = ecrUse;
	}

	public String getSubItemFlag() {
		return subItemFlag;
	}

	public void setSubItemFlag(String subItemFlag) {
		this.subItemFlag = subItemFlag;
	}

	public String getSubItem() {
		return subItem;
	}

	public void setSubItem(String subItem) {
		this.subItem = subItem;
	}

	public Long getRelationId() {
		return relationId;
	}

	public void setRelationId(Long relationId) {
		this.relationId = relationId;
	}

	public Long getItemSkuId() {
		return itemSkuId;
	}

	public void setItemSkuId(Long itemSkuId) {
		this.itemSkuId = itemSkuId;
	}

	public Long getItemEcrId() {
		return itemEcrId;
	}

	public void setItemEcrId(Long itemEcrId) {
		this.itemEcrId = itemEcrId;
	}


     

 }
