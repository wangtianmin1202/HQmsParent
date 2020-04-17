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
/**
 * @author KOCDZG0
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PqcProductionInfo {

	public String plantCode;
	public String line;
	public String workStation;
	public String inspectionNum;
	public String orderNo;
	public String itemCode;
	public String itemVersion;
}
