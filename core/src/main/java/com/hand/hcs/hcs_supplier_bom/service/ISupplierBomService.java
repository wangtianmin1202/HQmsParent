package com.hand.hcs.hcs_supplier_bom.service;

import java.util.List;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hcs.hcs_supplier_bom.dto.SupplierBom;

public interface ISupplierBomService extends IBaseService<SupplierBom>, ProxySelf<ISupplierBomService>{
	
	/**
	 * 获取有效数据
	 * @param supplierBom
	 * @return
	 */
	List<SupplierBom> queryInNow(SupplierBom supplierBom);
}