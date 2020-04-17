package com.hand.plm.plm_product_func_attr_approve.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.plm.plm_product_func_attr_approve.dto.ProductFuncAttrApprove;
import com.hand.plm.plm_product_func_attr_approve.view.ProductFuncAttrApproveVO;

public interface ProductFuncAttrApproveMapper extends Mapper<ProductFuncAttrApprove> {
	List<ProductFuncAttrApproveVO> queryNew(ProductFuncAttrApproveVO dto);
}