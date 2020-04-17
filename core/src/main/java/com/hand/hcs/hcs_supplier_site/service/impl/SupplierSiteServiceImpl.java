package com.hand.hcs.hcs_supplier_site.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hcs.hcs_supplier_site.dto.SupplierSite;
import com.hand.hcs.hcs_supplier_site.service.ISupplierSiteService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SupplierSiteServiceImpl extends BaseServiceImpl<SupplierSite> implements ISupplierSiteService{

}