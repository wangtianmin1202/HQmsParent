package com.hand.hap.webservice.ws.idto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author tainmin.wang
 * @version date：2019年11月11日 下午3:27:35
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Material {

	/**
	 * 物料编码
	 */
	public String MATNR; // 物料编码
	
	public String MAKTX; // 物料名称
	
	public String WERKS; // 工厂编码
	
	public String MEINS; // 单位
	
	public String MTART; // 物料类型
	
	public String BESKZ; // 自制/采购属性
	
	public String BSTRF; // 包装数
	
	public String STPRS; // 成本
	
	public String PEINH; // 价格单位
	
	public String MEINH; // 单位转换
	
	public String PLIFZ; //采购提前期
	

}
