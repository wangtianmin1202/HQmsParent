package com.hand.hqm.hqm_pqc_warning_his.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_pqc_warning_his.dto.PqcWarningHis;
import com.hand.hqm.hqm_pqc_warning_his.service.IPqcWarningHisService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PqcWarningHisServiceImpl extends BaseServiceImpl<PqcWarningHis> implements IPqcWarningHisService{

}