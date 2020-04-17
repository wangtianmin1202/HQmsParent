package com.hand.hcs.hcs_supplier_major_event.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hcs.hcs_supplier_major_event.dto.SupplierMajorEvent;
import com.hand.hcs.hcs_supplier_major_event.mapper.SupplierMajorEventMapper;
import com.hand.hcs.hcs_supplier_major_event.service.ISupplierMajorEventService;

@Service
@Transactional(rollbackFor = Exception.class)
public class SupplierMajorEventServiceImpl extends BaseServiceImpl<SupplierMajorEvent> implements ISupplierMajorEventService{
	
	@Autowired
	private SupplierMajorEventMapper mapper;
	
	@Override
	public List<SupplierMajorEvent> query(IRequest requestContext, SupplierMajorEvent dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.query(dto);
	}

	@Override
	public List<SupplierMajorEvent> changeFlag(IRequest requestContext, List<SupplierMajorEvent> dto) {
		for(SupplierMajorEvent supplierMajorEvent : dto) {
			SupplierMajorEvent majorEvent = new SupplierMajorEvent();
			majorEvent.setEventId(supplierMajorEvent.getEventId());
			majorEvent.setEnableFlag("N");
			self().updateByPrimaryKeySelective(requestContext, majorEvent);
		}
		return dto;
	}

}