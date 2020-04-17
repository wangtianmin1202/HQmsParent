package com.hand.itf.itf_supplier_materials.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.itf.itf_supplier_materials.dto.SupplierMaterials;
import com.hand.itf.itf_supplier_materials.service.ISupplierMaterialsService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SupplierMaterialsServiceImpl extends BaseServiceImpl<SupplierMaterials> implements ISupplierMaterialsService{

}