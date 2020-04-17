package com.hand.plm.plm_product_func_attr_draft.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.plm.plm_product_func_attr_approve.dto.ProductFuncAttrApprove;
import com.hand.plm.plm_product_func_attr_draft.dto.ProductFuncAttrDraft;

public interface IProductFuncAttrDraftService
		extends IBaseService<ProductFuncAttrDraft>, ProxySelf<IProductFuncAttrDraftService> {

	/**
	 * 检验产品和产品属性关系是否存在
	 * 
	 * @param irequest
	 * @param product
	 * @param productFunc
	 * @return
	 */
	boolean checkTreeLevelIsExist(IRequest irequest, String product, String productFunc);

	/**
	 * 校验产品属性是否已经存在草稿表中
	 * 
	 * @param irequest
	 * @param detailId 产品属性明细ID
	 * @return 如果存在，则返回草稿表KID,不存返回空
	 */
	String checkDetailDataIsExist(IRequest irequest, String detailId);

	/**
	 * 提交审批
	 * 
	 * @param irequest
	 * @param kidList
	 */
	List<ProductFuncAttrApprove> submitApprove(IRequest irequest, List<String> kidList);
}