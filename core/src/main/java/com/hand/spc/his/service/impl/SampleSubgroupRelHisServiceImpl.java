package com.hand.spc.his.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.spc.his.dto.SampleSubgroupRelHis;
import com.hand.spc.his.service.ISampleSubgroupRelHisService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SampleSubgroupRelHisServiceImpl extends BaseServiceImpl<SampleSubgroupRelHis> implements ISampleSubgroupRelHisService{

}