package com.hand.hqm.hqm_control_plan.service;

import java.util.List;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_control_plan.dto.ControlPlanFeatures;

public interface IControlPlanFeaturesService extends IBaseService<ControlPlanFeatures>, ProxySelf<IControlPlanFeaturesService>{
	List<ControlPlanFeatures> queryPrintData(ControlPlanFeatures ControlPlanFeatures);
	
	ControlPlanFeatures queryHeaderData(Long controlPlanId);
}