package com.hand.hqm.hqm_platform_program_his.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_platform_program_his.dto.PlatformProgramHis;
import com.hand.hqm.hqm_platform_program_his.service.IPlatformProgramHisService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PlatformProgramHisServiceImpl extends BaseServiceImpl<PlatformProgramHis> implements IPlatformProgramHisService{

}