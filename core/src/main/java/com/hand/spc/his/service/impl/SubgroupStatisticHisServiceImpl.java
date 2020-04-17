package com.hand.spc.his.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.spc.his.dto.SubgroupStatisticHis;
import com.hand.spc.his.service.ISubgroupStatisticHisService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SubgroupStatisticHisServiceImpl extends BaseServiceImpl<SubgroupStatisticHis> implements ISubgroupStatisticHisService{

}