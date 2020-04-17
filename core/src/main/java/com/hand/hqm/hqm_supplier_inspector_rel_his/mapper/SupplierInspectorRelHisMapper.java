package com.hand.hqm.hqm_supplier_inspector_rel_his.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_supplier_inspector_rel_his.dto.SupplierInspectorRelHis;

public interface SupplierInspectorRelHisMapper extends Mapper<SupplierInspectorRelHis>{

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月4日 
	 * @param dto
	 * @return
	 */
	List<SupplierInspectorRelHis> query(SupplierInspectorRelHis dto);

}