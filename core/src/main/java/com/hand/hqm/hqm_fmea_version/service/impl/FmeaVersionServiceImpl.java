package com.hand.hqm.hqm_fmea_version.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_fmea_version.dto.FmeaVersion;
import com.hand.hqm.hqm_fmea_version.service.IFmeaVersionService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class FmeaVersionServiceImpl extends BaseServiceImpl<FmeaVersion> implements IFmeaVersionService{

}