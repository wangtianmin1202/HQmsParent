package com.hand.hqm.hqm_standard_op_item_h_his.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_standard_op_item_h_his.dto.StandardOpItemHHis;
import com.hand.hqm.hqm_standard_op_item_h_his.service.IStandardOpItemHHisService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class StandardOpItemHHisServiceImpl extends BaseServiceImpl<StandardOpItemHHis> implements IStandardOpItemHHisService{

}