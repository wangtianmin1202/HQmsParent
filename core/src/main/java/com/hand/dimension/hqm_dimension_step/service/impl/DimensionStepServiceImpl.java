package com.hand.dimension.hqm_dimension_step.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.dimension.hqm_dimension_step.dto.DimensionStep;
import com.hand.dimension.hqm_dimension_step.service.IDimensionStepService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class DimensionStepServiceImpl extends BaseServiceImpl<DimensionStep> implements IDimensionStepService{

}