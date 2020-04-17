package com.hand.hqm.hqm_supplier_inspector_rel_his.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_supplier_inspector_rel_his.dto.SupplierInspectorRelHis;
import com.hand.hqm.hqm_supplier_inspector_rel_his.mapper.SupplierInspectorRelHisMapper;
import com.hand.hqm.hqm_supplier_inspector_rel_his.service.ISupplierInspectorRelHisService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SupplierInspectorRelHisServiceImpl extends BaseServiceImpl<SupplierInspectorRelHis> implements ISupplierInspectorRelHisService{

	@Autowired
	SupplierInspectorRelHisMapper mapper;
	/* (non-Javadoc)
	 * @see com.hand.hqm.hqm_supplier_inspector_rel_his.service.ISupplierInspectorRelHisService#query(com.hand.hap.core.IRequest, com.hand.hqm.hqm_supplier_inspector_rel_his.dto.SupplierInspectorRelHis, int, int)
	 */
	@Override
	public List<SupplierInspectorRelHis> query(IRequest requestContext, SupplierInspectorRelHis dto, int page,
			int pageSize) {
		// TODO Auto-generated method stub
		return mapper.query(dto);
	}

}