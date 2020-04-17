package com.hand.hcs.hcs_business_unit.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hcs.hcs_business_unit.dto.BusinessUnit;
import com.hand.hcs.hcs_business_unit.service.IBusinessUnitService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class BusinessUnitServiceImpl extends BaseServiceImpl<BusinessUnit> implements IBusinessUnitService{

}