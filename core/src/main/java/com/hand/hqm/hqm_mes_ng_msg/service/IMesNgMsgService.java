package com.hand.hqm.hqm_mes_ng_msg.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hap.util.SoapPostUtil.ResponseSap;
import com.hand.hap.webservice.ws.idto.ProductionNgInfo;
import com.hand.hqm.hqm_mes_ng_msg.dto.MesNgMsg;

public interface IMesNgMsgService extends IBaseService<MesNgMsg>, ProxySelf<IMesNgMsgService>{

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年2月27日 
	 * @param pni
	 * @return
	 */
	ResponseSap productionNgInfo(ProductionNgInfo pni);

}