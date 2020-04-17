package com.hand.hcs.hcs_supplier_bom.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hcs.hcs_supplier_bom.dto.SupplierBom;
import com.hand.hcs.hcs_supplier_bom.mapper.SupplierBomMapper;
import com.hand.hcs.hcs_supplier_bom.service.ISupplierBomService;

@Service
@Transactional(rollbackFor = Exception.class)
public class SupplierBomServiceImpl extends BaseServiceImpl<SupplierBom> implements ISupplierBomService{
	
	@Autowired
	private SupplierBomMapper mapper;
	
	@Override
	public List<SupplierBom> queryInNow(SupplierBom supplierBom) {
		// TODO Auto-generated method stub
		return mapper.queryInNow(supplierBom);
	}

}