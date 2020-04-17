package com.hand.hqm.hqm_control_plan.mapper;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_control_plan.dto.ControlPlanProcess;

public interface ControlPlanProcessMapper extends Mapper<ControlPlanProcess>{
	public ControlPlanProcess processNamecount(@Param("processName") String processName);

}