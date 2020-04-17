package com.hand.hqm.hqm_supp_item_exemption_his.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_supp_item_exemption_his.dto.SuppItemExemptionHis;
import com.hand.hqm.hqm_supp_item_exemption_his.service.ISuppItemExemptionHisService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SuppItemExemptionHisServiceImpl extends BaseServiceImpl<SuppItemExemptionHis> implements ISuppItemExemptionHisService{

}