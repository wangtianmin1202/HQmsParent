/**
 * 
 */
package com.hand.hap.webservice.ws.idto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
* @author tainmin.wang
* @version date：2020年2月25日 上午11:00:54
* 工单切换数据传输实体
*/

@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkOperationChange {
	
	public String plantCode;
	public String line;
	public String orderNo;
	public String itemCode;
	public String itemVersion;
	public String preOrder;
	public String preItemCode;
	public String preItemVersion;

}
