package com.hand.hap.webservice.ws.idto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * 供应商物料关系 来自Sap
* @author tainmin.wang
* @version date：2019年11月11日 上午9:27:18
* 
*/
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplierMaterialsTop {

	public List<SupplierMaterials> Sourcelist;
}
