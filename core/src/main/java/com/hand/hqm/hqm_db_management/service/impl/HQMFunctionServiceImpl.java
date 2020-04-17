package com.hand.hqm.hqm_db_management.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_db_management.dto.HQMFunction;
import com.hand.hqm.hqm_db_management.service.IHQMFunctionService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class HQMFunctionServiceImpl extends BaseServiceImpl<HQMFunction> implements IHQMFunctionService{

}