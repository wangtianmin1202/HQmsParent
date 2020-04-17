package com.hand.hcs.hcs_po_line_locations.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcs.hcs_po_line_locations.dto.PoLineLocations;

public interface PoLineLocationsMapper extends Mapper<PoLineLocations>{
	/**
	 * 送货单主界面查询
	 * @param poLineLocations
	 * @return
	 */
	List<PoLineLocations> queryPoLineLocations(PoLineLocations poLineLocations);
	/**
	 * 采购订单行查询
	 * @param poLineLocations
	 * @return
	 */
	List<PoLineLocations> queryPoLineDetail(PoLineLocations poLineLocations);
	/**
	 * 采购订单发运明细查询
	 * @param poLineLocations
	 * @return
	 */
	List<PoLineLocations> queryLocationDetail(PoLineLocations poLineLocations);
	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月15日 
	 * @param poHeaderId
	 * @param poLineId
	 * @return
	 */
	Float getNewPoLocationShipmentNum(Float poHeaderId, Float poLineId);
	
	
	Float getSumQuantity(Float poLineId);
}