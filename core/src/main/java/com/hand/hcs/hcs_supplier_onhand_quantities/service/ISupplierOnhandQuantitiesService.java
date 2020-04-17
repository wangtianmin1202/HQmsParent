package com.hand.hcs.hcs_supplier_onhand_quantities.service;

import java.text.ParseException;
import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hcs.hcs_supplier_onhand_quantities.dto.SupplierOnhandQuantities;

public interface ISupplierOnhandQuantitiesService extends IBaseService<SupplierOnhandQuantities>, ProxySelf<ISupplierOnhandQuantitiesService>{
	
	/**
	 * 供应商库存管理
	 * @param dto
	 * @param requestContext
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<SupplierOnhandQuantities> query(IRequest requestContext,SupplierOnhandQuantities dto, int page, int pageSize);
	/**
	 * 查询用户供应商
	 * @param requestContext
	 * @return
	 */
	ResponseData querySupplier(IRequest requestContext);
	/**
	 * 供应商库存管理查询（树形结构）
	 * @param requestContext 请求上下文
	 * @param dto 查询条件
	 * @param page 页码
	 * @param pageSize 页大小
	 * @return 结果集
	 */
	List<SupplierOnhandQuantities> queryTree(IRequest requestContext,SupplierOnhandQuantities dto, int page, int pageSize);
	/**
	 * 更新
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<SupplierOnhandQuantities> updateData(IRequest requestContext,List<SupplierOnhandQuantities> dto);
	/**
	 * 新增采购件
	 * @param requestContext 请求上下文
	 * @param dto 数据
	 * @return
	 */
	SupplierOnhandQuantities addData(IRequest requestContext,SupplierOnhandQuantities dto) throws ParseException;
	/**
	 * 删除
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	SupplierOnhandQuantities deleteData(IRequest requestContext,SupplierOnhandQuantities dto) throws ParseException;
}