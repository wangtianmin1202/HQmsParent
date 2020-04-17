package com.hand.hqm.hqm_measure_tool_msa_his.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_measure_tool_msa_his.dto.MeasureToolMsaHis;
import com.hand.hqm.hqm_measure_tool_msa_his.service.IMeasureToolMsaHisService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class MeasureToolMsaHisServiceImpl extends BaseServiceImpl<MeasureToolMsaHis> implements IMeasureToolMsaHisService{

}