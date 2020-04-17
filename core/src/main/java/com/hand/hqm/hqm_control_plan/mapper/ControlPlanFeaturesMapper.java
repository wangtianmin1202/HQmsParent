package com.hand.hqm.hqm_control_plan.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_control_plan.dto.ControlPlanFeatures;

public interface ControlPlanFeaturesMapper extends Mapper<ControlPlanFeatures>{
	List<ControlPlanFeatures> queryPrintData(ControlPlanFeatures ControlPlanFeatures);
	
	ControlPlanFeatures queryHeaderData(Long controlPlanId);

	List<ControlPlanFeatures> queryByControlPlanId(Long controlPlanId);
}