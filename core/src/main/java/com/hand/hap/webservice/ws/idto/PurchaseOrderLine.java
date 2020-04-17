package com.hand.hap.webservice.ws.idto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
* @author tainmin.wang
* @version date：2019年11月11日 下午3:28:06
* 
*/
@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseOrderLine {

	public String BELNR;//采购订单号
	
	public String WERKS;//行工厂编码
	
	public String POSEX;//行号
	
	public String IDTNR;//物料编码
	
	public String ANETW;//物料版本
	
	public String BMNG2;//订单行数量
	
	public String PMENE;//单位
	
	public String CURCY;//币种
	
	public String VPREI;//单价
	
	public String MWSKZ;//税率
	
	public String PEINH;//价格单位
	
	public String ACTION;//更新标识
	
	public String EDATU;//订单交期 sap的日期格式 yyyyMMdd
}
