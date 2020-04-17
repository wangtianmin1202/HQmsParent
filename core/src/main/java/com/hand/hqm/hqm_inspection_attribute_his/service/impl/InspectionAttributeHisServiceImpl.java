package com.hand.hqm.hqm_inspection_attribute_his.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_inspection_attribute_his.dto.InspectionAttributeHis;
import com.hand.hqm.hqm_inspection_attribute_his.service.IInspectionAttributeHisService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class InspectionAttributeHisServiceImpl extends BaseServiceImpl<InspectionAttributeHis> implements IInspectionAttributeHisService{

}