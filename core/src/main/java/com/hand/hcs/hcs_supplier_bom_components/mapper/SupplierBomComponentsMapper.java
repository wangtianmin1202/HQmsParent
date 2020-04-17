package com.hand.hcs.hcs_supplier_bom_components.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcs.hcs_supplier_bom_components.dto.SupplierBomComponents;

public interface SupplierBomComponentsMapper extends Mapper<SupplierBomComponents>{
	/**
	 * 获取最大物料号
	 * @return
	 */
	Integer queryMaxNum();
}