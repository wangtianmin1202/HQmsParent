package com.hand.hqm.hqm_control_plan.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hqm.hqm_control_plan.dto.ControlPlanFeatures;
import com.hand.hqm.hqm_control_plan.mapper.ControlPlanFeaturesMapper;
import com.hand.hqm.hqm_control_plan.service.IControlPlanFeaturesService;

@Service
@Transactional(rollbackFor = Exception.class)
public class ControlPlanFeaturesServiceImpl extends BaseServiceImpl<ControlPlanFeatures> implements IControlPlanFeaturesService{

	@Autowired
	ControlPlanFeaturesMapper controlPlanFeaturesMapper;
	@Override
	public List<ControlPlanFeatures> queryPrintData(ControlPlanFeatures ControlPlanFeatures) {
		return controlPlanFeaturesMapper.queryPrintData(ControlPlanFeatures);
	}
	@Override
	public ControlPlanFeatures queryHeaderData(Long controlPlanId) {
		return controlPlanFeaturesMapper.queryHeaderData(controlPlanId);
	}

}