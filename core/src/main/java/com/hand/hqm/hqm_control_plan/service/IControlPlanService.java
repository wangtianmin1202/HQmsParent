package com.hand.hqm.hqm_control_plan.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_control_plan.dto.ControlPlan;

public interface IControlPlanService extends IBaseService<ControlPlan>, ProxySelf<IControlPlanService>{
	public List<ControlPlan> selectControlPlan(IRequest requestCtx,ControlPlan controlPlan, int page, int pageSize);
	public List<ControlPlan> save(IRequest requestCtx,List<ControlPlan> list);
	public List<ControlPlan> commit(IRequest requestCtx,ControlPlan controlPlan);
	public int delete(List<ControlPlan> list);
}