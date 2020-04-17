package com.hand.spc.his.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.spc.his.dto.SampleSubgroupHis;
import com.hand.spc.his.service.ISampleSubgroupHisService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SampleSubgroupHisServiceImpl extends BaseServiceImpl<SampleSubgroupHis> implements ISampleSubgroupHisService{

}