package com.hand.spc.his.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.spc.his.dto.CountStatisticHis;
import com.hand.spc.his.service.ICountStatisticHisService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class CountStatisticHisServiceImpl extends BaseServiceImpl<CountStatisticHis> implements ICountStatisticHisService{

}