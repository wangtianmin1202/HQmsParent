package com.hand.hqm.hqm_online_sku_rule_his.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_online_sku_rule_his.dto.OnlineSkuRuleHis;
import com.hand.hqm.hqm_online_sku_rule_his.service.IOnlineSkuRuleHisService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class OnlineSkuRuleHisServiceImpl extends BaseServiceImpl<OnlineSkuRuleHis> implements IOnlineSkuRuleHisService{

}