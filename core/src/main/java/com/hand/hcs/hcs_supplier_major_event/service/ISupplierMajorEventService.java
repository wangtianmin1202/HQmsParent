package com.hand.hcs.hcs_supplier_major_event.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hcs.hcs_supplier_major_event.dto.SupplierMajorEvent;

public interface ISupplierMajorEventService extends IBaseService<SupplierMajorEvent>, ProxySelf<ISupplierMajorEventService>{
	/**
	 * 供应商重大调整 查询
	 * @param requestContext 请求上下文
	 * @param dto 条件
	 * @param page 页码
	 * @param pageSize 页大小
	 * @return 结果集
	 */
	List<SupplierMajorEvent> query(IRequest requestContext, SupplierMajorEvent dto, int page, int pageSize);
	/**
	 * 失效
	 * @param requestContext 请求上下文
	 * @param dto 失效数据
	 * @return 结果
	 */
	List<SupplierMajorEvent> changeFlag(IRequest requestContext, List<SupplierMajorEvent> dto);
}