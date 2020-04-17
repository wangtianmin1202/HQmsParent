/**
 * 
 */
package com.hand.hap.webservice.ws.idto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author tainmin.wang 产成品不合格信息
 * @version date：2020年2月27日 下午2:52:27
 * 
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductionNgInfo {

	public String plantCode;
	public String line;
	public String workStation;
	public String orderNo;
	public String itemCode;
	public String itemVersion;
	public String serialNo;
	public String ngCode;
	public String qty;
	public String eventTime;
}
