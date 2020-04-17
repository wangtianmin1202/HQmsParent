package com.hand.plm.spec_product_detail.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.plm.spec_product_detail.dto.SpecChangeOrder;
import com.hand.plm.spec_product_detail.dto.SpecProductDetail;

public interface ISpecChangeOrderService extends IBaseService<SpecChangeOrder>, ProxySelf<ISpecChangeOrderService>{
	String getChangeOrderCode();
	
	/**
	 * 查询变更数据
	 * @param request
	 * @return
	 */
	List<SpecChangeOrder> queryAll(IRequest request, SpecChangeOrder condition, int pageNum, int pageSize);
}