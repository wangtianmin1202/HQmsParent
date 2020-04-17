package com.hand.hqm.hqm_db_management.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_db_management.dto.HQMStructure;
import com.hand.hqm.hqm_db_management.service.IHQMStructureService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class HQMStructureServiceImpl extends BaseServiceImpl<HQMStructure> implements IHQMStructureService{

}