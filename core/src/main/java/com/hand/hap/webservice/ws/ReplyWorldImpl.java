package com.hand.hap.webservice.ws;

import javax.jws.WebService;

import com.hand.hap.webservice.ws.wsi.ReplyWorld;

/**
* @author tainmin.wang
* @version date：2019年11月6日 上午11:30:38
* 
*/
@WebService(endpointInterface = "com.hand.hap.webservice.ws.wsi.ReplyWorld",serviceName="ReplyGT")
public class ReplyWorldImpl implements ReplyWorld {

	@Override
	public String sayListen(String listen) {
		// TODO Auto-generated method stub
		return listen;
	}
}
