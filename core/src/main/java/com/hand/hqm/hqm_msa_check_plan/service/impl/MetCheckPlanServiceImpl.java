package com.hand.hqm.hqm_msa_check_plan.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hqm.hqm_msa_check_plan.dto.MetCheckPlan;
import com.hand.hqm.hqm_msa_check_plan.mapper.MetCheckPlanMapper;
import com.hand.hqm.hqm_msa_check_plan.service.IMetCheckPlanService;

@Service
@Transactional(rollbackFor = Exception.class)
public class MetCheckPlanServiceImpl extends BaseServiceImpl<MetCheckPlan> implements IMetCheckPlanService{

	@Autowired
	private MetCheckPlanMapper metCheckPlanMapper;
	@Override
	public List<MetCheckPlan> selectCheckPlan(MetCheckPlan metCheckPlan) {
		return metCheckPlanMapper.selectCheckPlan(metCheckPlan);
	}

}