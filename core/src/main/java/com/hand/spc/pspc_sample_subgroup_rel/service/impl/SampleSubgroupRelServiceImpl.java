package com.hand.spc.pspc_sample_subgroup_rel.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.spc.pspc_sample_subgroup_rel.dto.SampleSubgroupRel;
import com.hand.spc.pspc_sample_subgroup_rel.service.ISampleSubgroupRelService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SampleSubgroupRelServiceImpl extends BaseServiceImpl<SampleSubgroupRel> implements ISampleSubgroupRelService{

}