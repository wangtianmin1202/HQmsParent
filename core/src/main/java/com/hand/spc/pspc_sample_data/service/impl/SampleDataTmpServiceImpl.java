package com.hand.spc.pspc_sample_data.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.spc.pspc_sample_data.dto.SampleDataTmp;
import com.hand.spc.pspc_sample_data.service.ISampleDataTmpService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SampleDataTmpServiceImpl extends BaseServiceImpl<SampleDataTmp> implements ISampleDataTmpService{

}