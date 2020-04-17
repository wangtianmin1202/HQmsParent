package com.hand.hcs.hcs_suppliers.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hap.util.SoapPostUtil.Response;
import com.hand.hap.util.SoapPostUtil.ResponseSap;
import com.hand.hcs.hcs_suppliers.dto.Suppliers;
import com.hand.itf.itf_supplier_info.dto.SupplierInfo;

public interface ISuppliersService extends IBaseService<Suppliers>, ProxySelf<ISuppliersService>{
	
	/**
	 * 供方账号管理
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<Suppliers> query(IRequest requestContext, Suppliers dto, int page, int pageSize);
	/**
	 * 供方账号管理保存
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<Suppliers> save(IRequest requestContext,List<Suppliers> dto);
	/**
	 *供方账号管理 删除
	 * @param requestContext
	 * @param dto
	 */
	void deleteInfo(IRequest requestContext,List<Suppliers> dto);
	
	/**
	 * sap供应商接口传输
	 * @param sili
	 * @return
	 */
	ResponseSap transferSupplier(List<SupplierInfo> sili);
	
}