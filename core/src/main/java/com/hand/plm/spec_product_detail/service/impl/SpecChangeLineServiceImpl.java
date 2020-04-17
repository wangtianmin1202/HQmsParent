package com.hand.plm.spec_product_detail.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.plm.spec_product_detail.dto.SpecChangeLine;
import com.hand.plm.spec_product_detail.service.ISpecChangeLineService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SpecChangeLineServiceImpl extends BaseServiceImpl<SpecChangeLine> implements ISpecChangeLineService{

}