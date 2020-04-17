package com.hand.hcs.hcs_supplier_item_version.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hcs.hcs_supplier_item_version.dto.SupplierItemVersion;

public interface ISupplierItemVersionService extends IBaseService<SupplierItemVersion>, ProxySelf<ISupplierItemVersionService>{
	/**
	 * 失效
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<SupplierItemVersion> changeFlag(IRequest requestContext,List<SupplierItemVersion> dto);
	/**
	 * 版本明细查询
	 * @param requestContext 请求上下文
	 * @param dto 查询条件
	 * @param page 页码
	 * @param pageSize 页大小
	 * @return 结果集
	 */
	List<SupplierItemVersion> query(IRequest requestContext,SupplierItemVersion dto, int page, int pageSize);
	/**
	 * 修改主版本
	 * @param requestContext
	 * @param dto
	 */
	void changeMainVersion(IRequest requestContext,SupplierItemVersion dto);
}