package com.hand.spc.pspc_ce_parameter_relation.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.spc.pspc_ce_parameter_relation.dto.CeParameterRelation;
import com.hand.spc.pspc_ce_parameter_relation.service.ICeParameterRelationService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class CeParameterRelationServiceImpl extends BaseServiceImpl<CeParameterRelation> implements ICeParameterRelationService{

}