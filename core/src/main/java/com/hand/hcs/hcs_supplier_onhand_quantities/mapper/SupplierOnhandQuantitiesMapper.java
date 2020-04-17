package com.hand.hcs.hcs_supplier_onhand_quantities.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcs.hcs_supplier_onhand_quantities.dto.SupplierOnhandQuantities;

public interface SupplierOnhandQuantitiesMapper extends Mapper<SupplierOnhandQuantities>{
	
	/**
	 * 供应商库存管理查询 
	 * @param dto
	 * @return
	 */
	List<SupplierOnhandQuantities> query(SupplierOnhandQuantities dto);
	/**
	 * 供应商库存管理查询 （树形结构）
	 * @param dto 查询条件
	 * @return 结果集
	 */
	List<SupplierOnhandQuantities> queryTree(SupplierOnhandQuantities dto);
}