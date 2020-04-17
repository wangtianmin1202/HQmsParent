package com.hand.dimension.hqm_dimension_root_cause.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.dimension.hqm_dimension_order.service.IDimensionOrderService;
import com.hand.dimension.hqm_dimension_root_cause.dto.DimensionRootCause;
import com.hand.dimension.hqm_dimension_root_cause.service.IDimensionRootCauseService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class DimensionRootCauseServiceImpl extends BaseServiceImpl<DimensionRootCause> implements IDimensionRootCauseService{
	
	@Autowired
	IDimensionOrderService iDimensionOrderService;
	@Override
	public List<DimensionRootCause> saveOne(IRequest requestContext, DimensionRootCause dto) throws Exception {
		// TODO Auto-generated method stub
		DimensionRootCause updater = new DimensionRootCause();
		updater.setRcauseId(dto.getRcauseId());
		updater.setRootCause(dto.getRootCause());
		self().updateByPrimaryKeySelective(requestContext, updater);
		List<DimensionRootCause> lis = new ArrayList<>();
		lis.add(updater);
		iDimensionOrderService.changeStepStatus(requestContext, dto.getOrderId(), 4);
		return lis;
	}

	@Override
	public ResponseData commit(IRequest request, DimensionRootCause dto) throws Exception {
		// TODO Auto-generated method stub
		ResponseData responseData = new ResponseData();
		iDimensionOrderService.changeStep(request,dto.getOrderId(),4,5);
		return responseData;
	}

}