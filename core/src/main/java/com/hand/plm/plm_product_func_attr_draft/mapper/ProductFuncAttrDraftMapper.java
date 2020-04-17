package com.hand.plm.plm_product_func_attr_draft.mapper;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.plm.plm_product_func_attr_draft.dto.ProductFuncAttrDraft;

public interface ProductFuncAttrDraftMapper extends Mapper<ProductFuncAttrDraft> {
	/**
	 * 检查产品和产品属性的树层级关系
	 * 
	 * @param product
	 * @param productFunc
	 * @return
	 */
	Long checkTreeLevel(@Param("product") String product, @Param("productFunc") String productFunc);

	/**
	 * 获取产品属性明细版本号
	 * 
	 * @param specLineId
	 * @return
	 */
	String getDetailVersion(@Param("detailId") String detailId);
	
	String getChangeNum();

	/**
	 * 新增产品属性校验
	 * 
	 * @param specId
	 * @param functionContent
	 * @return
	 */
	Long insertDetailCheck(@Param("kid") String kid, @Param("functionContent") String functionContent);
	
}