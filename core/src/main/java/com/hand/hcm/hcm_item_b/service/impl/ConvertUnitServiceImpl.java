package com.hand.hcm.hcm_item_b.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hcm.hcm_item_b.dto.ConvertUnit;
import com.hand.hcm.hcm_item_b.service.IConvertUnitService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ConvertUnitServiceImpl extends BaseServiceImpl<ConvertUnit> implements IConvertUnitService{

}