package com.hand.hqm.hqm_mes_ng_recorde.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hap.util.SoapPostUtil.ResponseSap;
import com.hand.hap.webservice.ws.idto.SerialDisqualified;
import com.hand.hqm.hqm_mes_ng_recorde.dto.MesNgRecorde;

public interface IMesNgRecordeService extends IBaseService<MesNgRecorde>, ProxySelf<IMesNgRecordeService>{

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年2月25日 
	 * @param sdf
	 * @return
	 * @throws Exception 
	 */
	ResponseSap serialDisqualified(SerialDisqualified sdf) throws Exception;

}