package com.hand.hqm.hqm_fqc_sample_switch_his.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_fqc_sample_switch_his.dto.FqcSampleSwitchHis;
import com.hand.hqm.hqm_fqc_sample_switch_his.service.IFqcSampleSwitchHisService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class FqcSampleSwitchHisServiceImpl extends BaseServiceImpl<FqcSampleSwitchHis> implements IFqcSampleSwitchHisService{

}