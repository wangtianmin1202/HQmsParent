package com.hand.hcs.hcs_supplier_item_version.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hcs.hcs_supplier_item_version.dto.SupplierItemVersion;
import com.hand.hcs.hcs_supplier_item_version.mapper.SupplierItemVersionMapper;
import com.hand.hcs.hcs_supplier_item_version.service.ISupplierItemVersionService;

@Service
@Transactional(rollbackFor = Exception.class)
public class SupplierItemVersionServiceImpl extends BaseServiceImpl<SupplierItemVersion> implements ISupplierItemVersionService{
	
	@Autowired
	private SupplierItemVersionMapper mapper;
	
	@Override
	public List<SupplierItemVersion> changeFlag(IRequest requestContext, List<SupplierItemVersion> dto) {
		for(SupplierItemVersion supplierItemVersion : dto) {
			supplierItemVersion.setEnableFlag("N");
			self().updateByPrimaryKeySelective(requestContext, supplierItemVersion);
		}
		return dto;
	}

	@Override
	public List<SupplierItemVersion> query(IRequest requestContext, SupplierItemVersion dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.query(dto);
	}

	@Override
	public void changeMainVersion(IRequest requestContext, SupplierItemVersion dto) {
		// TODO Auto-generated method stub
		SupplierItemVersion supplierItemVersion = new SupplierItemVersion();
		supplierItemVersion.setKid(dto.getKid());
		supplierItemVersion = mapper.selectByPrimaryKey(supplierItemVersion);
		if(supplierItemVersion != null && !dto.getMainVersion().equals(supplierItemVersion.getMainVersion())) {
			supplierItemVersion.setMainVersion(dto.getMainVersion());
			self().updateByPrimaryKeySelective(requestContext, supplierItemVersion);
		}
	}

}