package com.hand.hap.webservice.ws.idto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
* @author tainmin.wang
* @version date：2019年11月11日 下午3:28:06
* 
*/
@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseOrderShp {
	
	public String PARTN; //供应商编码
}
