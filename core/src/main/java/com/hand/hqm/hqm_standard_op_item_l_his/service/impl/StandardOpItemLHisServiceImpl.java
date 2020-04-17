package com.hand.hqm.hqm_standard_op_item_l_his.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_standard_op_item_l_his.dto.StandardOpItemLHis;
import com.hand.hqm.hqm_standard_op_item_l_his.service.IStandardOpItemLHisService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class StandardOpItemLHisServiceImpl extends BaseServiceImpl<StandardOpItemLHis> implements IStandardOpItemLHisService{

}