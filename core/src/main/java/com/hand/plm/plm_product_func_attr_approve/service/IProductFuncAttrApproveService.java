package com.hand.plm.plm_product_func_attr_approve.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.plm.plm_product_func_attr_approve.dto.ProductFuncAttrApprove;
import com.hand.plm.plm_product_func_attr_approve.view.ProductFuncAttrApproveVO;
import com.hand.plm.product_func_attr_basic.view.ProductFuncAttrDetailVO;

public interface IProductFuncAttrApproveService
		extends IBaseService<ProductFuncAttrApprove>, ProxySelf<IProductFuncAttrApproveService> {

	List<ProductFuncAttrApproveVO> queryNew(IRequest request, ProductFuncAttrApproveVO vo, int pageNum, int pageSize);
}