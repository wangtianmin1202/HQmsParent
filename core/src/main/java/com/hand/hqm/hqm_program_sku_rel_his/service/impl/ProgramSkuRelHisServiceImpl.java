package com.hand.hqm.hqm_program_sku_rel_his.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_program_sku_rel_his.dto.ProgramSkuRelHis;
import com.hand.hqm.hqm_program_sku_rel_his.service.IProgramSkuRelHisService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProgramSkuRelHisServiceImpl extends BaseServiceImpl<ProgramSkuRelHis> implements IProgramSkuRelHisService{

}