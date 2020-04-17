package com.hand.spc.repository.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.repository.dto.CoefficientR;
import com.hand.spc.repository.service.ICoefficientRService;

@Service
@Transactional(rollbackFor = Exception.class)
public class CoefficientRServiceImpl extends BaseServiceImpl<CoefficientR> implements ICoefficientRService {
}
