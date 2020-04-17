package com.hand.hcs.hcs_barcode_relation.dto;

import java.util.List;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hand.hap.system.dto.BaseDTO;

@ExtensionAttribute(disable = true)
@Table(name = "HCS_BARCODE_RELATION")
public class BarcodeRelation extends BaseDTO {

	public static final String FIELD_BARCODE_RELATION_ID = "barcodeRelationId";
	public static final String FIELD_OUTBARCODE_ID = "outbarcodeId";
	public static final String FIELD_OUTBARCODE_TYPE = "outbarcodeType";
	public static final String FIELD_INBARCODE_ID = "inbarcodeId";
	public static final String FIELD_INBARCODE_TYPE = "inbarcodeType";
	public static final String FIELD_ENABLE_FLAG = "enableFlag";
	public static final String FIELD_TICKET_ID = "ticketId";
	public static final String FIELD_TICKET_LINE_ID = "ticketLineId";

	@Id
	@GeneratedValue
	private Float barcodeRelationId;

	private Float outbarcodeId;

	@Length(max = 30)
	private String outbarcodeType;

	private Float inbarcodeId;

	@Length(max = 30)
	private String inbarcodeType;

	@Length(max = 1)
	private String enableFlag;

	private Float ticketId;

	private Float ticketLineId;

	@Transient
	private Float itemId;
	@Transient
	private Float plantId;
	@Transient
	private String itemVersion;
	@Transient
	private String lotCode;
	@Transient
	private List<Float> rqObarcodeIdList;

	@Transient
	private String outbarcodeContent;
	
	@Transient
	private String inbarcodeContent;
	

	public String getOutbarcodeContent() {
		return outbarcodeContent;
	}

	public void setOutbarcodeContent(String outbarcodeContent) {
		this.outbarcodeContent = outbarcodeContent;
	}

	public String getInbarcodeContent() {
		return inbarcodeContent;
	}

	public void setInbarcodeContent(String inbarcodeContent) {
		this.inbarcodeContent = inbarcodeContent;
	}

	public List<Float> getRqObarcodeIdList() {
		return rqObarcodeIdList;
	}

	public void setRqObarcodeIdList(List<Float> rqObarcodeIdList) {
		this.rqObarcodeIdList = rqObarcodeIdList;
	}

	public Float getItemId() {
		return itemId;
	}

	public void setItemId(Float itemId) {
		this.itemId = itemId;
	}

	public Float getPlantId() {
		return plantId;
	}

	public void setPlantId(Float plantId) {
		this.plantId = plantId;
	}

	public String getItemVersion() {
		return itemVersion;
	}

	public void setItemVersion(String itemVersion) {
		this.itemVersion = itemVersion;
	}

	public String getLotCode() {
		return lotCode;
	}

	public void setLotCode(String lotCode) {
		this.lotCode = lotCode;
	}

	public void setBarcodeRelationId(Float barcodeRelationId) {
		this.barcodeRelationId = barcodeRelationId;
	}

	public Float getBarcodeRelationId() {
		return barcodeRelationId;
	}

	public void setOutbarcodeId(Float outbarcodeId) {
		this.outbarcodeId = outbarcodeId;
	}

	public Float getOutbarcodeId() {
		return outbarcodeId;
	}

	public void setOutbarcodeType(String outbarcodeType) {
		this.outbarcodeType = outbarcodeType;
	}

	public String getOutbarcodeType() {
		return outbarcodeType;
	}

	public void setInbarcodeId(Float inbarcodeId) {
		this.inbarcodeId = inbarcodeId;
	}

	public Float getInbarcodeId() {
		return inbarcodeId;
	}

	public void setInbarcodeType(String inbarcodeType) {
		this.inbarcodeType = inbarcodeType;
	}

	public String getInbarcodeType() {
		return inbarcodeType;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}

	public String getEnableFlag() {
		return enableFlag;
	}

	public void setTicketId(Float ticketId) {
		this.ticketId = ticketId;
	}

	public Float getTicketId() {
		return ticketId;
	}

	public void setTicketLineId(Float ticketLineId) {
		this.ticketLineId = ticketLineId;
	}

	public Float getTicketLineId() {
		return ticketLineId;
	}

}
