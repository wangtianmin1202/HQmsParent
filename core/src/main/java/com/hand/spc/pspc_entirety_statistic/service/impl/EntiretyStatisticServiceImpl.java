package com.hand.spc.pspc_entirety_statistic.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.spc.pspc_entirety_statistic.dto.EntiretyStatistic;
import com.hand.spc.pspc_entirety_statistic.service.IEntiretyStatisticService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class EntiretyStatisticServiceImpl extends BaseServiceImpl<EntiretyStatistic> implements IEntiretyStatisticService{

}