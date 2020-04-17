package com.hand.hcs.hcs_supplier_bom.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcs.hcs_supplier_bom.dto.SupplierBom;

public interface SupplierBomMapper extends Mapper<SupplierBom>{
	/**
	 * 获取有效数据
	 * @param supplierBom
	 * @return
	 */
	List<SupplierBom> queryInNow(SupplierBom supplierBom);
}