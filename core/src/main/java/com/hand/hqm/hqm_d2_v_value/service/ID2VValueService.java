package com.hand.hqm.hqm_d2_v_value.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_d2_v_value.dto.D2VValue;

public interface ID2VValueService extends IBaseService<D2VValue>, ProxySelf<ID2VValueService>{
	/**
	 * 求临界值
	 * @return
	 */
	double getTinv(D2VValue dto);
}