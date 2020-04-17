package com.hand.spc.ecr_main.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.ecr_main.dto.EcrTechnicalFileHeader;
import com.hand.spc.ecr_main.service.IEcrTechnicalFileHeaderService;
import org.springframework.stereotype.Service;
import com.hand.spc.ecr_main.dto.EcrTechnicalFileHeader;
import com.hand.spc.ecr_main.service.IEcrTechnicalFileHeaderService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class EcrTechnicalFileHeaderServiceImpl extends BaseServiceImpl<EcrTechnicalFileHeader> implements IEcrTechnicalFileHeaderService {

}