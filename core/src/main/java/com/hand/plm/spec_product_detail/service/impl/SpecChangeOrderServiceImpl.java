package com.hand.plm.spec_product_detail.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.plm.spec_product_detail.dto.SpecChangeOrder;
import com.hand.plm.spec_product_detail.dto.SpecProductDetail;
import com.hand.plm.spec_product_detail.mapper.SpecChangeOrderMapper;
import com.hand.plm.spec_product_detail.service.ISpecChangeOrderService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SpecChangeOrderServiceImpl extends BaseServiceImpl<SpecChangeOrder> implements ISpecChangeOrderService{

	@Autowired
	private SpecChangeOrderMapper changeOrderMapper;
	
	 /**
	 * 生成变更编码
	 */
	@Override
	public synchronized String getChangeOrderCode() {
	    String yearLast = new SimpleDateFormat("yy",Locale.CHINESE).format(Calendar.getInstance().getTime());
	    return "SPEC" + yearLast + changeOrderMapper.getChangeOrderCode();
	}
	
	public void changeOrderData(IRequest iRequest, List<SpecProductDetail> details) {
		
	}
	
	@Override
	public List<SpecChangeOrder> queryAll(IRequest requestContext,SpecChangeOrder condition, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		return changeOrderMapper.queryAll(condition);
	}
}