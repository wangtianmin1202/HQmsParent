package com.hand.hqm.hqm_msa_check_plan.service;

import java.util.List;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_msa_check_plan.dto.MetCheckPlan;

public interface IMetCheckPlanService extends IBaseService<MetCheckPlan>, ProxySelf<IMetCheckPlanService>{
	public List<MetCheckPlan> selectCheckPlan(MetCheckPlan metCheckPlan);
}