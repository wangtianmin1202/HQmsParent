package com.hand.spc.his.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.spc.his.dto.EntiretyStatisticHis;
import com.hand.spc.his.service.IEntiretyStatisticHisService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class EntiretyStatisticHisServiceImpl extends BaseServiceImpl<EntiretyStatisticHis> implements IEntiretyStatisticHisService{

}