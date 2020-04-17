package com.hand.hcm.hcm_production_line.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hcm.hcm_production_line.dto.ProductionLine;
import com.hand.hcm.hcm_production_line.service.IProductionLineService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProductionLineServiceImpl extends BaseServiceImpl<ProductionLine> implements IProductionLineService{

}