package com.hand.plm.plm_product_basic_data.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.plm.plm_product_basic_data.dto.ProductBasicData;
import com.hand.plm.plm_product_basic_data.service.IProductBasicDataService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProductBasicDataServiceImpl extends BaseServiceImpl<ProductBasicData> implements IProductBasicDataService{

}