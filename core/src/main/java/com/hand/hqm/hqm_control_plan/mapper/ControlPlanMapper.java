package com.hand.hqm.hqm_control_plan.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_control_plan.dto.ControlPlan;

public interface ControlPlanMapper extends Mapper<ControlPlan> {
	public List<ControlPlan> selectControlPlan(ControlPlan controlPlan);
	
	public int deleteFeaturesByControlPlan(Long controlPlanId);

	public List<ControlPlan> queryControlPlanByLevelId(Long levelId);
}