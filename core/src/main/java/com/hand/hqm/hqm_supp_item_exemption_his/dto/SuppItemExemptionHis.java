package com.hand.hqm.hqm_supp_item_exemption_his.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import com.hand.hap.system.dto.BaseDTO;
import java.util.Date;

@ExtensionAttribute(disable = true)
@Table(name = "HQM_SUPP_ITEM_EXEMPTION_HIS")
public class SuppItemExemptionHis extends BaseDTO {

	@Id
	@GeneratedValue
	private Float eventId;

	private Long eventBy;

	private Date eventTime;

	private Float exemptionId;

	private Float plantId;

	private Float itemId;

	private Float supplierId;

	private Float supplierSiteId;

	private Date exemptionTimeFrom;

	private Date exemptionTimeTo;

	@Length(max = 30)
	private String exemptionFlag;

	@Length(max = 30)
	private String enableFlag;

	@Length(max = 200)
	private String remark;

	public void setEventId(Float eventId) {
		this.eventId = eventId;
	}

	public Float getEventId() {
		return eventId;
	}

	public void setEventBy(Long eventBy) {
		this.eventBy = eventBy;
	}

	public Long getEventBy() {
		return eventBy;
	}

	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public void setExemptionId(Float exemptionId) {
		this.exemptionId = exemptionId;
	}

	public Float getExemptionId() {
		return exemptionId;
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

	public void setSupplierSiteId(Float supplierSiteId) {
		this.supplierSiteId = supplierSiteId;
	}

	public Float getSupplierSiteId() {
		return supplierSiteId;
	}

	public void setExemptionTimeFrom(Date exemptionTimeFrom) {
		this.exemptionTimeFrom = exemptionTimeFrom;
	}

	public Date getExemptionTimeFrom() {
		return exemptionTimeFrom;
	}

	public void setExemptionTimeTo(Date exemptionTimeTo) {
		this.exemptionTimeTo = exemptionTimeTo;
	}

	public Date getExemptionTimeTo() {
		return exemptionTimeTo;
	}

	public void setExemptionFlag(String exemptionFlag) {
		this.exemptionFlag = exemptionFlag;
	}

	public String getExemptionFlag() {
		return exemptionFlag;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}

	public String getEnableFlag() {
		return enableFlag;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemark() {
		return remark;
	}

}
