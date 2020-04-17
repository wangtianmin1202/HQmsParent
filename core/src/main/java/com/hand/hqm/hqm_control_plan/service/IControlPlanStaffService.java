package com.hand.hqm.hqm_control_plan.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_control_plan.dto.ControlPlanStaff;

public interface IControlPlanStaffService extends IBaseService<ControlPlanStaff>, ProxySelf<IControlPlanStaffService>{
	ResponseData save(List<ControlPlanStaff> dto,IRequest requestCtx);
}