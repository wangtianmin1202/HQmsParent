package com.hand.hqm.hqm_switch_rule.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_switch_rule.dto.SwitchRule;
import com.hand.hqm.hqm_switch_rule.service.ISwitchRuleService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SwitchRuleServiceImpl extends BaseServiceImpl<SwitchRule> implements ISwitchRuleService{

}