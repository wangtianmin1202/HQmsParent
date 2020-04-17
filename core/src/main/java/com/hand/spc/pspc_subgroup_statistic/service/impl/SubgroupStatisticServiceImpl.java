package com.hand.spc.pspc_subgroup_statistic.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.spc.pspc_subgroup_statistic.dto.SubgroupStatistic;
import com.hand.spc.pspc_subgroup_statistic.service.ISubgroupStatisticService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SubgroupStatisticServiceImpl extends BaseServiceImpl<SubgroupStatistic> implements ISubgroupStatisticService{

}