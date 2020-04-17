package com.hand.npi.npi_route.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.npi.npi_route.dto.TechnologyWpSpecDetail;
import com.hand.npi.npi_route.service.ITechnologyWpSpecDetailService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class TechnologyWpSpecDetailServiceImpl extends BaseServiceImpl<TechnologyWpSpecDetail> implements ITechnologyWpSpecDetailService{

}