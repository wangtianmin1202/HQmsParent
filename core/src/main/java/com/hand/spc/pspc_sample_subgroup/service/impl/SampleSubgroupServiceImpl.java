package com.hand.spc.pspc_sample_subgroup.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.spc.pspc_sample_subgroup.dto.SampleSubgroup;
import com.hand.spc.pspc_sample_subgroup.service.ISampleSubgroupService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SampleSubgroupServiceImpl extends BaseServiceImpl<SampleSubgroup> implements ISampleSubgroupService{

}