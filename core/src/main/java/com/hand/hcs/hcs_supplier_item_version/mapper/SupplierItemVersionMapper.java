package com.hand.hcs.hcs_supplier_item_version.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcs.hcs_supplier_item_version.dto.SupplierItemVersion;

public interface SupplierItemVersionMapper extends Mapper<SupplierItemVersion>{
	/**
	 * 版本明细查询
	 * @param supplierItemVersion
	 * @return
	 */
	List<SupplierItemVersion> query(SupplierItemVersion supplierItemVersion);
}