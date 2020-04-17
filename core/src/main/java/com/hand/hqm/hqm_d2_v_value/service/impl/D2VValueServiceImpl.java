package com.hand.hqm.hqm_d2_v_value.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_d2_v_value.dto.D2VValue;
import com.hand.hqm.hqm_d2_v_value.service.ID2VValueService;
import com.hand.hqm.hqm_utils.Tinvs;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class D2VValueServiceImpl extends BaseServiceImpl<D2VValue> implements ID2VValueService{

	@Override
	public double getTinv(D2VValue dto) {
		return Tinvs.Tinv(dto.getA(),dto.getV().doubleValue());
	}

}