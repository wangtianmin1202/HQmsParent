package com.hand.spc.hqm_d_order.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.hqm_d_order.mapper.EightDOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.spc.hqm_d_order.dto.EightDOrder;
import com.hand.spc.hqm_d_order.service.IEightDOrderService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class EightDOrderServiceImpl extends BaseServiceImpl<EightDOrder> implements IEightDOrderService{
    @Autowired
    private EightDOrderMapper eightDOrderMapper;
    @Override
    public Long getSeqInter() {
        return eightDOrderMapper.getSeqInter();
    }
}