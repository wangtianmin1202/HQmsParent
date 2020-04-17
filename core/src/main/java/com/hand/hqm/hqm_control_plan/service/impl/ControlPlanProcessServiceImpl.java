package com.hand.hqm.hqm_control_plan.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_control_plan.dto.ControlPlanProcess;
import com.hand.hqm.hqm_control_plan.service.IControlPlanProcessService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ControlPlanProcessServiceImpl extends BaseServiceImpl<ControlPlanProcess> implements IControlPlanProcessService{

}