package com.hand.hap.webservice.ws.wsi;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @author tainmin.wang
 * @version date：2019年11月6日 上午11:28:31
 * 
 */
@WebService
public interface DataReceptionFromSap {
	@WebMethod
	String SRMGlobalFunc(@WebParam(name = "FuncName") String FuncName,
			@WebParam(name = "JsonString") String JsonString);

}
