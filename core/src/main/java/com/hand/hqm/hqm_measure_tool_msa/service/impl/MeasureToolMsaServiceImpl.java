package com.hand.hqm.hqm_measure_tool_msa.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_measure_tool_msa.dto.MeasureToolMsa;
import com.hand.hqm.hqm_measure_tool_msa.service.IMeasureToolMsaService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class MeasureToolMsaServiceImpl extends BaseServiceImpl<MeasureToolMsa> implements IMeasureToolMsaService{

}