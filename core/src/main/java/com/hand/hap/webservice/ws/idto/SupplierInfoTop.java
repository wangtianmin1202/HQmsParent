package com.hand.hap.webservice.ws.idto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * sap供应商信息
 * @author tainmin.wang
 * @version date：2019年11月8日 下午12:09:17
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplierInfoTop { //
	public List<SupplierInfo> Vendor;
}
