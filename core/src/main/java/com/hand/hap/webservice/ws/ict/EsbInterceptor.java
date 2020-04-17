/**
 * 
 */
package com.hand.hap.webservice.ws.ict;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import java.util.List;

import javax.xml.namespace.QName;

/**
 * @description 自定义soap拦截器
 * @author tainmin.wang
 * @version date：2019年11月26日 下午6:13:20
 * 
 */
public class EsbInterceptor extends AbstractPhaseInterceptor<SoapMessage> {

	/**
	 * @param phase
	 */
	public EsbInterceptor(String phase) {
		super(phase);
	}
	public EsbInterceptor() {
		super(Phase.RECEIVE);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.cxf.interceptor.Interceptor#handleMessage(org.apache.cxf.message.
	 * Message)
	 */
	@Override
	public void handleMessage(SoapMessage message) throws Fault {
		
		System.out.println( message + "end-------------------------------------" + message.getExchange());
		throw new Fault(new Exception("IP no match"));
	}

}
