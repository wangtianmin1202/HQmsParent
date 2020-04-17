package com.hand.hcs.hcs_supplier_major_event.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcs.hcs_supplier_major_event.dto.SupplierMajorEvent;

public interface SupplierMajorEventMapper extends Mapper<SupplierMajorEvent>{
	/**
	 * 供应商重大调整 查询
	 * @param supplierMajorEvent 条件
	 * @return 查询结果集
	 */
	List<SupplierMajorEvent> query(SupplierMajorEvent supplierMajorEvent);
}