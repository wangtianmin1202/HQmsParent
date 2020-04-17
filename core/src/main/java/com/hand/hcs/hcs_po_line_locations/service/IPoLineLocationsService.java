package com.hand.hcs.hcs_po_line_locations.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hcs.hcs_po_line_locations.dto.PoLineLocations;

public interface IPoLineLocationsService extends IBaseService<PoLineLocations>, ProxySelf<IPoLineLocationsService>{
	/**
	 * 采购订单明细保存
	 * @param requestContext
	 * @param dto
	 */
	void saveInfo(IRequest requestContext, List<PoLineLocations> dto);
	/**
	 * 送货单主界面查询
	 * @param requestContext
	 * @param poLineLocations
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<PoLineLocations> queryPoLineLocations(IRequest requestContext,PoLineLocations poLineLocations, int page, int pageSize);
	/**
	 * 采购订单行查询
	 * @param requestContext
	 * @param poLineLocations
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<PoLineLocations> queryPoLineDetail(IRequest requestContext,PoLineLocations poLineLocations, int page, int pageSize);
	/**
	 * 发运计划查询 关闭
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<PoLineLocations> close(IRequest requestContext, List<PoLineLocations> dto);
	
	/**
	 * 采购订单发运明细查询
	 * @param requestContext
	 * @param poLineLocations
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<PoLineLocations> queryLocationDetail(IRequest requestContext,PoLineLocations poLineLocations, int page, int pageSize);
}