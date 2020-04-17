package com.hand.hap.webservice.ws.wsi;

import javax.jws.WebService;

/**
 * @author tainmin.wang
 * @version date：2019年11月6日 上午11:28:31
 * 
 */
@WebService
public interface DataReceptionFromWms {

	/**
	 * @description 总掉方法
	 * @author tianmin.wang
	 * @date 2019年11月28日 
	 * @param FuncName
	 * @param JsonString
	 * @return
	 */
	String SRMGlobalFunc(String FuncName, String JsonString);
	/**
	 * 
	 * @description ws接口方法 wms 容器信息 传输 已融合至总掉方法
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param listen
	 * @return
	 */
	//String transferContainer(String listen);

	/**
	 * 
	 * @description ws接口方法 wms 送货单收货信息 传输 已融合至总掉方法
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param listen
	 * @return
	 */
	//String transferDeliveryReceipt(String listen);

	/**
	 * 
	 * @description ws接口方法 wms 退货单执行信息 传输 已融合至总掉方法
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param listen
	 * @return
	 */
	//String transferReturnDeliveryExecute(String listen);
}
