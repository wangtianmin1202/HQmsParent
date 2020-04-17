package com.hand.hcs.hcs_out_barcode.dto;

import java.util.Date;
import java.util.List;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hand.hap.system.dto.BaseDTO;
import com.hand.hcs.hcs_barcode.dto.SmallBarcode;

@ExtensionAttribute(disable = true)
@Table(name = "HCS_OUT_BARCODE")
public class OutBarcode extends BaseDTO {

	public static final String FIELD_OBARCODE_ID = "obarcodeId";
	public static final String FIELD_OBARCODE = "obarcode";
	public static final String FIELD_OBARCODE_TYPE = "obarcodeType";
	public static final String FIELD_STATUS = "status";
	public static final String FIELD_PACKING_SIZE = "packingSize";
	public static final String FIELD_PACKING_LEVEL = "packingLevel";
	public static final String FIELD_REMARKS = "remarks";
	public static final String FIELD_ENABLE_FLAG = "enableFlag";
	public static final String FIELD_FREE_FLAG = "freeFlag";
	public static final String FIELD_PACKING_FLAG = "packingFlag";
	public static final String FIELD_PRINT_TIME = "printTime";

	@Id
	@GeneratedValue
	private Float obarcodeId;

	@Length(max = 100)
	private String obarcode;

	@Length(max = 30)
	private String obarcodeType;

	@Length(max = 10)
	private String status;

	@Length(max = 30)
	private String packingSize;

	@Length(max = 30)
	private String packingLevel;

	@Length(max = 240)
	private String remarks;

	@Length(max = 1)
	private String enableFlag;

	@Length(max = 1)
	private String freeFlag;

	@Length(max = 1)
	private String packingFlag;

	private Float printTime;

	private Date creationDate;

	private Float supplierId;

	private Float suppliersSiteId;

	@Length(max = 1)
	private String obarcodeNum;
	
	private Float plantId;
	
	private Date lastUpdateDate;
	@Transient
	private Float itemId;
	@Transient
	private Float ticketId;
	@Transient
	private Float ticketLineId;
	@Transient
	private Float poHeaderId;
	@Transient
	private Float poLineId;
	@Transient
	private Float lineLocationId;
	@Transient
	private Float refundOrderId;
	@Transient
	private Float obarcodeControlId;
	@Transient
	private String ticketNumber;// 送货单号
	@Transient
	private Float ticketLineNum;// 送货单行号
	@Transient
	private String poNumber;// 采购订单号
	@Transient
	private Float shipmentNum;// 发运号
	@Transient
	private String supplierCode;// 供应商编码
	@Transient
	private String supplierName;// 供应商名称
	@Transient
	private String supplersSiteName;// 供应商地点
	@Transient
	private Float quantity;// 小箱条码数
	@Transient
	private String startTime;// 创建时间从
	@Transient
	private String endTime;// 创建时间至
	@Transient
	private Long codeSum;// 条码张数
	@Transient
	private Long countSmallCode;// 小箱条码数
	@Transient
	private List<SmallBarcode> smallBarcodeList;// 小箱条码
	@Transient
	private String sbarcode;//物料标签序列
	@Transient
	private String plantCode;
	@Transient
	private String plantName;
	@Transient
	private String barcodeContent;//物料标签
	@Transient
	private String tpObarcodeNum;//托盘序列
	@Transient
	private String tpObarcode;//托盘标签
	@Transient
	private String obarcodeTypeDesc;
	@Transient 
	private String itemName;//物料名称
	@Transient 
	private String itemCode;//物料编码
	@Transient
	private String itemVersion;//物料版本
	@Transient
	private String uom;//单位
	@Transient
	private Date productionTime;//生产日期
	@Transient
	private String lotCode;//批次
	@Transient
	private String caseNumber;//箱号
	@Transient
	private String purchaseGroup;//采购组
	@Transient
	private Float barcodeQty;

	public Float getBarcodeQty() {
		return barcodeQty;
	}

	public void setBarcodeQty(Float barcodeQty) {
		this.barcodeQty = barcodeQty;
	}

	public Float getItemId() {
		return itemId;
	}

	public void setItemId(Float itemId) {
		this.itemId = itemId;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getPurchaseGroup() {
		return purchaseGroup;
	}

	public void setPurchaseGroup(String purchaseGroup) {
		this.purchaseGroup = purchaseGroup;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getLotCode() {
		return lotCode;
	}

	public void setLotCode(String lotCode) {
		this.lotCode = lotCode;
	}

	public String getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemVersion() {
		return itemVersion;
	}

	public void setItemVersion(String itemVersion) {
		this.itemVersion = itemVersion;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public Date getProductionTime() {
		return productionTime;
	}

	public void setProductionTime(Date productionTime) {
		this.productionTime = productionTime;
	}

	public String getObarcodeTypeDesc() {
		return obarcodeTypeDesc;
	}

	public void setObarcodeTypeDesc(String obarcodeTypeDesc) {
		this.obarcodeTypeDesc = obarcodeTypeDesc;
	}

	public String getSbarcode() {
		return sbarcode;
	}

	public void setSbarcode(String sbarcode) {
		this.sbarcode = sbarcode;
	}

	public String getPlantCode() {
		return plantCode;
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}

	public String getPlantName() {
		return plantName;
	}

	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}

	public String getBarcodeContent() {
		return barcodeContent;
	}

	public void setBarcodeContent(String barcodeContent) {
		this.barcodeContent = barcodeContent;
	}

	public String getTpObarcodeNum() {
		return tpObarcodeNum;
	}

	public void setTpObarcodeNum(String tpObarcodeNum) {
		this.tpObarcodeNum = tpObarcodeNum;
	}

	public String getTpObarcode() {
		return tpObarcode;
	}

	public void setTpObarcode(String tpObarcode) {
		this.tpObarcode = tpObarcode;
	}

	public Float getPlantId() {
		return plantId;
	}

	public void setPlantId(Float plantId) {
		this.plantId = plantId;
	}

	public String getObarcodeNum() {
		return obarcodeNum;
	}

	public void setObarcodeNum(String obarcodeNum) {
		this.obarcodeNum = obarcodeNum;
	}

	public List<SmallBarcode> getSmallBarcodeList() {
		return smallBarcodeList;
	}

	public void setSmallBarcodeList(List<SmallBarcode> smallBarcodeList) {
		this.smallBarcodeList = smallBarcodeList;
	}

	public Long getCountSmallCode() {
		return countSmallCode;
	}

	public void setCountSmallCode(Long countSmallCode) {
		this.countSmallCode = countSmallCode;
	}

	public Long getCodeSum() {
		return codeSum;
	}

	public void setCodeSum(Long codeSum) {
		this.codeSum = codeSum;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Float getQuantity() {
		return quantity;
	}

	public void setQuantity(Float quantity) {
		this.quantity = quantity;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Float getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Float supplierId) {
		this.supplierId = supplierId;
	}

	public Float getSuppliersSiteId() {
		return suppliersSiteId;
	}

	public void setSuppliersSiteId(Float suppliersSiteId) {
		this.suppliersSiteId = suppliersSiteId;
	}

	public Float getTicketId() {
		return ticketId;
	}

	public void setTicketId(Float ticketId) {
		this.ticketId = ticketId;
	}

	public Float getTicketLineId() {
		return ticketLineId;
	}

	public void setTicketLineId(Float ticketLineId) {
		this.ticketLineId = ticketLineId;
	}

	public Float getPoHeaderId() {
		return poHeaderId;
	}

	public void setPoHeaderId(Float poHeaderId) {
		this.poHeaderId = poHeaderId;
	}

	public Float getPoLineId() {
		return poLineId;
	}

	public void setPoLineId(Float poLineId) {
		this.poLineId = poLineId;
	}

	public Float getLineLocationId() {
		return lineLocationId;
	}

	public void setLineLocationId(Float lineLocationId) {
		this.lineLocationId = lineLocationId;
	}

	public Float getRefundOrderId() {
		return refundOrderId;
	}

	public void setRefundOrderId(Float refundOrderId) {
		this.refundOrderId = refundOrderId;
	}

	public Float getObarcodeControlId() {
		return obarcodeControlId;
	}

	public void setObarcodeControlId(Float obarcodeControlId) {
		this.obarcodeControlId = obarcodeControlId;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public Float getTicketLineNum() {
		return ticketLineNum;
	}

	public void setTicketLineNum(Float ticketLineNum) {
		this.ticketLineNum = ticketLineNum;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public Float getShipmentNum() {
		return shipmentNum;
	}

	public void setShipmentNum(Float shipmentNum) {
		this.shipmentNum = shipmentNum;
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

	public String getSupplersSiteName() {
		return supplersSiteName;
	}

	public void setSupplersSiteName(String supplersSiteName) {
		this.supplersSiteName = supplersSiteName;
	}

	public void setObarcodeId(Float obarcodeId) {
		this.obarcodeId = obarcodeId;
	}

	public Float getObarcodeId() {
		return obarcodeId;
	}

	public void setObarcode(String obarcode) {
		this.obarcode = obarcode;
	}

	public String getObarcode() {
		return obarcode;
	}

	public void setObarcodeType(String obarcodeType) {
		this.obarcodeType = obarcodeType;
	}

	public String getObarcodeType() {
		return obarcodeType;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setPackingSize(String packingSize) {
		this.packingSize = packingSize;
	}

	public String getPackingSize() {
		return packingSize;
	}

	public void setPackingLevel(String packingLevel) {
		this.packingLevel = packingLevel;
	}

	public String getPackingLevel() {
		return packingLevel;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}

	public String getEnableFlag() {
		return enableFlag;
	}

	public void setFreeFlag(String freeFlag) {
		this.freeFlag = freeFlag;
	}

	public String getFreeFlag() {
		return freeFlag;
	}

	public void setPackingFlag(String packingFlag) {
		this.packingFlag = packingFlag;
	}

	public String getPackingFlag() {
		return packingFlag;
	}

	public void setPrintTime(Float printTime) {
		this.printTime = printTime;
	}

	public Float getPrintTime() {
		return printTime;
	}

}