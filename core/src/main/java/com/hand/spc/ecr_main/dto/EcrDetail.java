package com.hand.spc.ecr_main.dto;

import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;
import com.hand.spc.ecr_main.view.EcrDetailsVO;
import groovy.transform.ToString;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@ExtensionAttribute(disable=true)
@Table(name = "hpm_ecr_item_suppliers")
@ToString
public class EcrDetail extends BaseDTO {

	 
	 @Id
	 @GeneratedValue
	 private Long kid;
 
	 
	 private Long ecrItemKid;

    /**
     * hpm_ecr_influencedmaterial  -----kid
     */
	 @Transient
	 private Long heiKid;
	 
	 @Transient
     private String ecrno;
     
     @Transient
	 private Long materialId;
	 
     @Transient
	 private String itemDescription;
	 
     
	 private String buyer;
	 
     @Transient
	 private String supplier;
	 
	 private Float poQty;
	 
	 private Float supplierOnhand;
	 
	 private Float specialOnhand;
	 
	 private Float calculateOnhand;
	 
	 private String supplierId;
	 
	 
	 private String itemVersion;
	 
	 @Transient
	 private String itemCode;
	 
	 @Transient
	 private String supplierName;
	
	 @Transient
	 private BigDecimal itemPrice;
	 
	 @Transient
	 @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	 private Date endDate;
	 
	 @Transient
	 private BigDecimal purchaseLeadTime;
	 
	 @Transient
	 private BigDecimal moq;

	 private Float specialWipOnhand  ;
	 
	 @Transient
	 private Float itemQty;

	 
	 
	public Float getItemQty() {
		return itemQty;
	}

	public void setItemQty(Float itemQty) {
		this.itemQty = itemQty;
	}

	public Float getSpecialWipOnhand() {
		return specialWipOnhand;
	}

	public void setSpecialWipOnhand(Float specialWipOnhand) {
		this.specialWipOnhand = specialWipOnhand;
	}

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public BigDecimal getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(BigDecimal itemPrice) {
		this.itemPrice = itemPrice;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public BigDecimal getPurchaseLeadTime() {
		return purchaseLeadTime;
	}

	public void setPurchaseLeadTime(BigDecimal purchaseLeadTime) {
		this.purchaseLeadTime = purchaseLeadTime;
	}

	public BigDecimal getMoq() {
		return moq;
	}

	public void setMoq(BigDecimal moq) {
		this.moq = moq;
	}

	public Long getKid() {
			return kid;
	}

	public void setKid(Long kid) {
			this.kid = kid;
	}

	public String getEcrno() {
		return ecrno;
	}

	public Long getMaterialId() {
		return materialId;
	}

	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public Float getPoQty() {
		return poQty;
	}

	public void setPoQty(Float poQty) {
		this.poQty = poQty;
	}

	public Float getSupplierOnhand() {
		return supplierOnhand;
	}

	public void setSupplierOnhand(Float supplierOnhand) {
		this.supplierOnhand = supplierOnhand;
	}

	public Float getSpecialOnhand() {
		return specialOnhand;
	}

	public void setSpecialOnhand(Float specialOnhand) {
		this.specialOnhand = specialOnhand;
	}

	public Float getCalculateOnhand() {
		return calculateOnhand;
	}

	public void setCalculateOnhand(Float calculateOnhand) {
		this.calculateOnhand = calculateOnhand;
	}

	public void setEcrno(String ecrno) {
		this.ecrno = ecrno;
	}

	public String getItemVersion() {
		return itemVersion;
	}

	public void setItemVersion(String itemVersion) {
		this.itemVersion = itemVersion;
	}

    public Long getEcrItemKid() {
        return ecrItemKid;
    }

    public void setEcrItemKid(Long ecrItemKid) {
        this.ecrItemKid = ecrItemKid;
    }

    public Long getHeiKid() {
        return heiKid;
    }

    public void setHeiKid(Long heiKid) {
        this.heiKid = heiKid;
    }
}
