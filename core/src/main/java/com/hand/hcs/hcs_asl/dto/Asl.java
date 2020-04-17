package com.hand.hcs.hcs_asl.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hand.hap.system.dto.BaseDTO;

@ExtensionAttribute(disable = true)
@Table(name = "HCS_ASL")
public class Asl extends BaseDTO {

	public static final String FIELD_ASL_ID = "aslId";
	public static final String FIELD_PLANT_ID = "plantId";
	public static final String FIELD_ITEM_ID = "itemId";
	public static final String FIELD_SUPPLIER_ID = "supplierId";
	public static final String FIELD_SUPPLERS_SITE_ID = "supplersSiteId";
	public static final String FIELD_ENABLE_FLAG = "enableFlag";

	@Id
	@GeneratedValue
	private Float aslId;

	private Float plantId;

	private Float itemId;

	private Float supplierId;

	private Float supplersSiteId;

	@Length(max = 1)
	private String enableFlag;

	@Transient
	private Float urgentLeadTime;
	@Transient
	private Float totalCapacity;

	@Transient
	private String itemCode;// 物料描述

	@Transient
	private String itemName;// 物料描述

	@Transient
	private String primaryUom;// 单位

	@Transient
	private String supplierCode;// 供应商代码

	@Transient
	private String supplierName;// 供应商名称

	@Transient
	private String siteDescription;// 供应商地点

	@Transient
	private String supplierSiteCode;// 供应商地点编码

	@Transient
	private String plantName;// 库存组织

	@Transient
	private String plantCode;// 工厂code

	@Transient
	private Float supplierSiteId;

	@Transient
	private Float supplierSitId;

	@Transient
	private String purchaseGroup;

	@Transient
	private String name;

	@Transient
	private String purchaseType;

	@Transient
	private Float leadTime;

	@Transient
	private String leadTimeHave;

	public String getLeadTimeHave() {
		return leadTimeHave;
	}

	public void setLeadTimeHave(String leadTimeHave) {
		this.leadTimeHave = leadTimeHave;
	}

	public Float getUrgentLeadTime() {
		return urgentLeadTime;
	}

	public Float getTotalCapacity() {
		return totalCapacity;
	}

	public void setUrgentLeadTime(Float urgentLeadTime) {
		this.urgentLeadTime = urgentLeadTime;
	}

	public void setTotalCapacity(Float totalCapacity) {
		this.totalCapacity = totalCapacity;
	}

	public String getPurchaseGroup() {
		return purchaseGroup;
	}

	public void setPurchaseGroup(String purchaseGroup) {
		this.purchaseGroup = purchaseGroup;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPurchaseType() {
		return purchaseType;
	}

	public void setPurchaseType(String purchaseType) {
		this.purchaseType = purchaseType;
	}

	public Float getLeadTime() {
		return leadTime;
	}

	public void setLeadTime(Float leadTime) {
		this.leadTime = leadTime;
	}

	public String getSupplierSiteCode() {
		return supplierSiteCode;
	}

	public void setSupplierSiteCode(String supplierSiteCode) {
		this.supplierSiteCode = supplierSiteCode;
	}

	public void setAslId(Float aslId) {
		this.aslId = aslId;
	}

	public Float getAslId() {
		return aslId;
	}

	public void setPlantId(Float plantId) {
		this.plantId = plantId;
	}

	public Float getPlantId() {
		return plantId;
	}

	public void setItemId(Float itemId) {
		this.itemId = itemId;
	}

	public Float getItemId() {
		return itemId;
	}

	public void setSupplierId(Float supplierId) {
		this.supplierId = supplierId;
	}

	public Float getSupplierId() {
		return supplierId;
	}

	public void setSupplersSiteId(Float supplersSiteId) {
		this.supplersSiteId = supplersSiteId;
	}

	public Float getSupplersSiteId() {
		return supplersSiteId;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}

	public String getEnableFlag() {
		return enableFlag;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getPrimaryUom() {
		return primaryUom;
	}

	public void setPrimaryUom(String primaryUom) {
		this.primaryUom = primaryUom;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getSiteDescription() {
		return siteDescription;
	}

	public void setSiteDescription(String siteDescription) {
		this.siteDescription = siteDescription;
	}

	public String getPlantName() {
		return plantName;
	}

	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}

	public String getPlantCode() {
		return plantCode;
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}

	public Float getSupplierSiteId() {
		return supplierSiteId;
	}

	public void setSupplierSiteId(Float supplierSiteId) {
		this.supplierSiteId = supplierSiteId;
	}

	public Float getSupplierSitId() {
		return supplierSitId;
	}

	public void setSupplierSitId(Float supplierSitId) {
		this.supplierSitId = supplierSitId;
	}

}
