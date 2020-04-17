package com.hand.hap.webservice.ws.idto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
* @author tainmin.wang
* @version date：2019年11月11日 下午3:28:06
* 
*/
@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseOrderAsn {
	public String BELNR;//采购订单号
	public String BSART;//采购订单类型
	public String DATUM;//发布时间
}
