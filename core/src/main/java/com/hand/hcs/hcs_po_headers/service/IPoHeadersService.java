package com.hand.hcs.hcs_po_headers.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hcs.hcs_po_headers.dto.PoHeaders;

public interface IPoHeadersService extends IBaseService<PoHeaders>, ProxySelf<IPoHeadersService>{
	
	/**
	 *  查询 业务实体、库存组织、供应商、供应商地点
	 * @param poHeaders
	 * @return
	 */
	List<PoHeaders> queryUtil(IRequest requestContext, PoHeaders poHeaders);
	
	/**
	 *  采购订单头查询
	 * @param requestContext
	 * @param poHeaders
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<PoHeaders> query(IRequest requestContext, PoHeaders poHeaders, int page, int pageSize);
	
	/**
	 * 采购订单确认
	 * @param requestContext
	 * @param headList
	 * @return
	 */
	List<PoHeaders> confirm(IRequest requestContext, List<PoHeaders> headList);
	/**
	 * 校验发运行
	 * @param requestContext
	 * @param headList
	 * @return
	 */
	List<PoHeaders> checkLine(IRequest requestContext, List<PoHeaders> headList);
}