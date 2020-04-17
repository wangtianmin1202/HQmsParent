package com.hand.npi.npi_route.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.npi.npi_route.dto.TechnologyWpStoreDetail;
import com.hand.npi.npi_route.service.ITechnologyWpStoreDetailService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class TechnologyWpStoreDetailServiceImpl extends BaseServiceImpl<TechnologyWpStoreDetail> implements ITechnologyWpStoreDetailService{

}