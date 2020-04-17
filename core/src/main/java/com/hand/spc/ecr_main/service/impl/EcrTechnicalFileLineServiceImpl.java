package com.hand.spc.ecr_main.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.ecr_main.dto.EcrTechnicalFileLine;
import com.hand.spc.ecr_main.service.IEcrTechnicalFileLineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class EcrTechnicalFileLineServiceImpl extends BaseServiceImpl<EcrTechnicalFileLine> implements IEcrTechnicalFileLineService {

}