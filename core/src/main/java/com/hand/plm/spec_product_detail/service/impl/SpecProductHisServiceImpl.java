package com.hand.plm.spec_product_detail.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.plm.spec_product_detail.dto.SpecProductHis;
import com.hand.plm.spec_product_detail.service.ISpecProductHisService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SpecProductHisServiceImpl extends BaseServiceImpl<SpecProductHis> implements ISpecProductHisService{

}