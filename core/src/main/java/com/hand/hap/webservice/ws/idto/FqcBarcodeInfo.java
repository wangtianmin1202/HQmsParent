/**
 * 
 */
package com.hand.hap.webservice.ws.idto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
* @author tainmin.wang
* @version date：2020年2月25日 上午9:53:13
* 
*/
@JsonIgnoreProperties(ignoreUnknown = true)
public class FqcBarcodeInfo {
	public String plantCode;
	public String orderNo;
	public String itemCode;
	public String itemVersion;
	public String serialNo;
	public String isSample;
}
