package com.hand.hqm.hqm_supplier_inspector_rel.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_supplier_inspector_rel.dto.SupplierInspectorRel;

public interface SupplierInspectorRelMapper extends Mapper<SupplierInspectorRel>{

	/**
	 * 
	 * @description 主界面查询
	 * @author tianmin.wang
	 * @date 2019年12月16日 
	 * @param dto
	 * @return
	 */
	List<SupplierInspectorRel> reSelect(SupplierInspectorRel dto);

	/**
	 * @description 批量更新
	 * @author tianmin.wang
	 * @date 2019年12月16日 
	 * @param dto
	 */
	void batchUpdateIns(SupplierInspectorRel dto);

	/**
	 * @description 批量更新
	 * @author tianmin.wang
	 * @date 2019年12月16日 
	 * @param dto
	 */
	void batchUpdatePre(SupplierInspectorRel dto);

	/**
	 * @description 批量更新
	 * @author tianmin.wang
	 * @date 2019年12月16日 
	 * @param dto
	 */
	void batchUpdateSqe(SupplierInspectorRel dto);
}