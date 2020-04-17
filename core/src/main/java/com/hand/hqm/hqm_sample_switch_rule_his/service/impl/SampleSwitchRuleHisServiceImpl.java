package com.hand.hqm.hqm_sample_switch_rule_his.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_sample_switch_rule_his.dto.SampleSwitchRuleHis;
import com.hand.hqm.hqm_sample_switch_rule_his.service.ISampleSwitchRuleHisService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SampleSwitchRuleHisServiceImpl extends BaseServiceImpl<SampleSwitchRuleHis> implements ISampleSwitchRuleHisService{

}