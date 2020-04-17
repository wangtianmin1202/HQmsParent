package com.hand.itf.itf_supplier_info.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.itf.itf_supplier_info.dto.SupplierInfo;
import com.hand.itf.itf_supplier_info.service.ISupplierInfoService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SupplierInfoServiceImpl extends BaseServiceImpl<SupplierInfo> implements ISupplierInfoService{

}