package com.hand.plm.plm_product_series.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.plm.plm_product_series.dto.ProductSeries;
import com.hand.plm.plm_product_series.service.IProductSeriesService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProductSeriesServiceImpl extends BaseServiceImpl<ProductSeries> implements IProductSeriesService{

}