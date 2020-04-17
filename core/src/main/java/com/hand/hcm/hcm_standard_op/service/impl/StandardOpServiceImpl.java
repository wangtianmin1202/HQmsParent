package com.hand.hcm.hcm_standard_op.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hcm.hcm_standard_op.dto.StandardOp;
import com.hand.hcm.hcm_standard_op.service.IStandardOpService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class StandardOpServiceImpl extends BaseServiceImpl<StandardOp> implements IStandardOpService{

}