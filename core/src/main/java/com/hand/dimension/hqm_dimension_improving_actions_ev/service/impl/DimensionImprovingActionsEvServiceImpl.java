package com.hand.dimension.hqm_dimension_improving_actions_ev.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.dimension.hqm_dimension_improving_actions_ev.dto.DimensionImprovingActionsEv;
import com.hand.dimension.hqm_dimension_improving_actions_ev.service.IDimensionImprovingActionsEvService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class DimensionImprovingActionsEvServiceImpl extends BaseServiceImpl<DimensionImprovingActionsEv> implements IDimensionImprovingActionsEvService{

}