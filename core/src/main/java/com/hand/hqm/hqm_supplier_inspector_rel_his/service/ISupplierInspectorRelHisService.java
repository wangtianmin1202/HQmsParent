package com.hand.hqm.hqm_supplier_inspector_rel_his.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_supplier_inspector_rel_his.dto.SupplierInspectorRelHis;

public interface ISupplierInspectorRelHisService extends IBaseService<SupplierInspectorRelHis>, ProxySelf<ISupplierInspectorRelHisService>{

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月4日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<SupplierInspectorRelHis> query(IRequest requestContext, SupplierInspectorRelHis dto, int page, int pageSize);

}