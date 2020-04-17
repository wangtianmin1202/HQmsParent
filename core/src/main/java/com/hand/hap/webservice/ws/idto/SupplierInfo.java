package com.hand.hap.webservice.ws.idto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * sap供应商信息
 * @author tainmin.wang
 * @version date：2019年11月8日 下午12:09:17
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplierInfo { //
	public String LIFNR;
	public String NAME1;
	public String ZTERM;
}
