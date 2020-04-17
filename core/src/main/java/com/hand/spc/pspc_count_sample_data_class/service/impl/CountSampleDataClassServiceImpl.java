package com.hand.spc.pspc_count_sample_data_class.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.spc.pspc_count_sample_data_class.dto.CountSampleDataClass;
import com.hand.spc.pspc_count_sample_data_class.service.ICountSampleDataClassService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class CountSampleDataClassServiceImpl extends BaseServiceImpl<CountSampleDataClass> implements ICountSampleDataClassService{

}