package com.hand.npi.npi_technology.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.npi.npi_technology.dto.ComposeProductMateriel;
import com.hand.npi.npi_technology.service.IComposeProductMaterielService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ComposeProductMaterielServiceImpl extends BaseServiceImpl<ComposeProductMateriel> implements IComposeProductMaterielService{

}