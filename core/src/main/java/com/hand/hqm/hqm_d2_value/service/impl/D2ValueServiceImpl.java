package com.hand.hqm.hqm_d2_value.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_d2_value.dto.D2Value;
import com.hand.hqm.hqm_d2_value.service.ID2ValueService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class D2ValueServiceImpl extends BaseServiceImpl<D2Value> implements ID2ValueService{

}