package com.hand.hqm.hqm_mes_ng_matiral.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hap.util.SoapPostUtil.ResponseSap;
import com.hand.hap.webservice.ws.idto.MaterialNgInfo;
import com.hand.hqm.hqm_mes_ng_matiral.dto.MesNgMatiral;

public interface IMesNgMatiralService extends IBaseService<MesNgMatiral>, ProxySelf<IMesNgMatiralService>{

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年2月27日 
	 * @param mni
	 * @return
	 */
	ResponseSap materialNgInfo(MaterialNgInfo mni);

}