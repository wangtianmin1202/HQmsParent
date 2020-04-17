package com.hand.spc.hqm_d_order.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.spc.hqm_d_order.dto.DProblemDescription;
import com.hand.spc.hqm_d_order.service.IDProblemDescriptionService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class DProblemDescriptionServiceImpl extends BaseServiceImpl<DProblemDescription> implements IDProblemDescriptionService{

}