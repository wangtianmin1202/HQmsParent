package com.hand.npi.npi_technology.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.npi.npi_technology.dto.EbomDetail;
import com.hand.npi.npi_technology.service.IEbomDetailService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class EbomDetailServiceImpl extends BaseServiceImpl<EbomDetail> implements IEbomDetailService{

}