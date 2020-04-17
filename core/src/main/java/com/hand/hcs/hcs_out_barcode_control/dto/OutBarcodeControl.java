package com.hand.hcs.hcs_out_barcode_control.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import com.hand.hap.system.dto.BaseDTO;
@ExtensionAttribute(disable=true)
@Table(name = "HCS_OUT_BARCODE_CONTROL")
public class OutBarcodeControl extends BaseDTO {

     public static final String FIELD_OBARCODE_ID = "obarcodeId";
     public static final String FIELD_SUPPLIER_ID = "supplierId";
     public static final String FIELD_SUPPLERS_SITE_ID = "supplersSiteId";
     public static final String FIELD_TICKET_ID = "ticketId";
     public static final String FIELD_TICKET_LINE_ID = "ticketLineId";
     public static final String FIELD_PO_HEADER_ID = "poHeaderId";
     public static final String FIELD_PO_LINE_ID = "poLineId";
     public static final String FIELD_LINE_LOCATION_ID = "lineLocationId";
     public static final String FIELD_REFUND_ORDER_ID = "refundOrderId";
     public static final String FIELD_OBARCODE_CONTROL_ID = "obarcodeControlId";


     private Float obarcodeId;

     private Float supplierId;

     private Float suppliersSiteId;

     private Float ticketId;

     private Float ticketLineId;

     private Float poHeaderId;

     private Float poLineId;

     private Float lineLocationId;

     private Float refundOrderId;

     @Id
     @GeneratedValue
     private Float obarcodeControlId;


     public void setObarcodeId(Float obarcodeId){
         this.obarcodeId = obarcodeId;
     }

     public Float getObarcodeId(){
         return obarcodeId;
     }

     public void setSupplierId(Float supplierId){
         this.supplierId = supplierId;
     }

     public Float getSupplierId(){
         return supplierId;
     }

     

     public Float getSuppliersSiteId() {
		return suppliersSiteId;
	}

	public void setSuppliersSiteId(Float suppliersSiteId) {
		this.suppliersSiteId = suppliersSiteId;
	}

	public void setTicketId(Float ticketId){
         this.ticketId = ticketId;
     }

     public Float getTicketId(){
         return ticketId;
     }

     public void setTicketLineId(Float ticketLineId){
         this.ticketLineId = ticketLineId;
     }

     public Float getTicketLineId(){
         return ticketLineId;
     }

     public void setPoHeaderId(Float poHeaderId){
         this.poHeaderId = poHeaderId;
     }

     public Float getPoHeaderId(){
         return poHeaderId;
     }

     public void setPoLineId(Float poLineId){
         this.poLineId = poLineId;
     }

     public Float getPoLineId(){
         return poLineId;
     }

     public void setLineLocationId(Float lineLocationId){
         this.lineLocationId = lineLocationId;
     }

     public Float getLineLocationId(){
         return lineLocationId;
     }

     public void setRefundOrderId(Float refundOrderId){
         this.refundOrderId = refundOrderId;
     }

     public Float getRefundOrderId(){
         return refundOrderId;
     }

     public void setObarcodeControlId(Float obarcodeControlId){
         this.obarcodeControlId = obarcodeControlId;
     }

     public Float getObarcodeControlId(){
         return obarcodeControlId;
     }

     }
