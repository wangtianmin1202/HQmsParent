package com.hand.hqm.hqm_sop_pqc_control_l.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_sop_pqc_control_l.dto.SopPqcControlL;
import com.hand.hqm.hqm_sop_pqc_control_l.service.ISopPqcControlLService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SopPqcControlLServiceImpl extends BaseServiceImpl<SopPqcControlL> implements ISopPqcControlLService{

}