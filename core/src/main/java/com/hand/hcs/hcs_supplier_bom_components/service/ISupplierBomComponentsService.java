package com.hand.hcs.hcs_supplier_bom_components.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hcs.hcs_supplier_bom_components.dto.SupplierBomComponents;

public interface ISupplierBomComponentsService extends IBaseService<SupplierBomComponents>, ProxySelf<ISupplierBomComponentsService>{
	
	/**
	 * 关键件校验
	 * @param requestCotext
	 * @param dto
	 * @return
	 */
	SupplierBomComponents checkData(IRequest requestCotext,SupplierBomComponents dto) throws RuntimeException;
	/**
	 * 新增关键件
	 * @param requestCotext
	 * @param dto
	 * @return
	 */
	SupplierBomComponents addData(IRequest requestCotext,SupplierBomComponents dto);
	/**
	 * 删除
	 * @param requestCotext
	 * @param dto
	 * @return
	 */
	SupplierBomComponents deleteData(IRequest requestCotext,SupplierBomComponents dto);
}