package com.hand.hap.webservice.ws.idto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * 供应商物料关系 来自Sap
* @author tainmin.wang
* @version date：2019年11月11日 上午9:27:18
* 
*/
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplierMaterials {

	public String WERKS;//工厂编码
	public String MATNR;//物料编码
	
	public String LIFNR;//供应商编码
	public String VDATU;//有效期从 yyyyMMdd
	
	public String BDATU;//有效期至 yyyyMMdd
	public String EKGRP;//采购组代码
	
	public String UNTTO;//允差上限
	public String UEBTO;//允差下限
	public String APLFZ;//采购提前期
}
