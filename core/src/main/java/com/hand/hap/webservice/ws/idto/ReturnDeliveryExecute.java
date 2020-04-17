package com.hand.hap.webservice.ws.idto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * 退货单执行接口实体类
 * @author tainmin.wang
 * @version date：2019年11月6日 下午2:12:41  WMS
 */
public class ReturnDeliveryExecute {
	private String returnOrder;
	
	private String returnLineNo;
	
	private String returnQty;
	
	private String returnDate;
	
	private String remarks1;
	
	private String remarks2;
	
	private String remarks3;
	
	private String remarks4;
	
	private String remarks5;
	
	public String getReturnOrder() {
		return returnOrder;
	}
	public void setReturnOrder(String returnOrder) {
		this.returnOrder = returnOrder;
	}
	public String getReturnLineNo() {
		return returnLineNo;
	}
	public void setReturnLineNo(String returnLineNo) {
		this.returnLineNo = returnLineNo;
	}
	public String getReturnQty() {
		return returnQty;
	}
	public void setReturnQty(String returnQty) {
		this.returnQty = returnQty;
	}
	public String getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}
	public String getRemarks1() {
		return remarks1;
	}
	public void setRemarks1(String remarks1) {
		this.remarks1 = remarks1;
	}
	public String getRemarks2() {
		return remarks2;
	}
	public void setRemarks2(String remarks2) {
		this.remarks2 = remarks2;
	}
	public String getRemarks3() {
		return remarks3;
	}
	public void setRemarks3(String remarks3) {
		this.remarks3 = remarks3;
	}
	public String getRemarks4() {
		return remarks4;
	}
	public void setRemarks4(String remarks4) {
		this.remarks4 = remarks4;
	}
	public String getRemarks5() {
		return remarks5;
	}
	public void setRemarks5(String remarks5) {
		this.remarks5 = remarks5;
	}
}
