package com.hand.hcs.hcs_refund_order.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hcs.hcs_refund_order.dto.RefundOrderH;
import com.hand.hcs.hcs_refund_order.mapper.RefundOrderHMapper;
import com.hand.hcs.hcs_refund_order.service.IRefundOrderHService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class RefundOrderHServiceImpl extends BaseServiceImpl<RefundOrderH> implements IRefundOrderHService{
	
	@Autowired
	private RefundOrderHMapper mapper;
	
	@Override
	public List<RefundOrderH> query(IRequest reuqestContext, RefundOrderH dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.query(dto);
	}

}