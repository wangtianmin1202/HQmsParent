/**
 * 
 */
package com.hand.hap.webservice.ws.idto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * 工单发起FQC 下线报检申请
* @author tainmin.wang 
* @version date：2020年2月25日 上午9:53:13
* 
*/
@JsonIgnoreProperties(ignoreUnknown = true)
public class FqcOutLineInspection {
	public String plantCode; //默认CNKE
	public String line;//产线
	public String orderNo;//工单号
	public String orderType;//订单类型
	public String itemCode;//物料编号
	public String vtpNumber;//VTP编号
	public String itemVersion;//物料版本
	public String qty;//生产数量
	public String productDate;//生产日期 yyyy-MM-dd HH:mm:ss
	public String preOrderNo;//原工单号
	public String componentItem;
	public String componentVersion;
}
