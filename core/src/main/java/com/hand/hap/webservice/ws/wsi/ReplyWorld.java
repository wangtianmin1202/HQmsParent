package com.hand.hap.webservice.ws.wsi;

import javax.jws.WebService;

/**
 * @description 自我测试自调用webservice
 * @author tainmin.wang
 * @version date：2019年11月6日 上午11:28:31
 * 
 */
@WebService
public interface ReplyWorld {
	/**
	 * 
	 * @description 收到什么返回什么
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param listen
	 * @return
	 */
	String sayListen(String listen);
}
