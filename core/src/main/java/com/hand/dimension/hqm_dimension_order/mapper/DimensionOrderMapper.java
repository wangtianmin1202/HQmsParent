package com.hand.dimension.hqm_dimension_order.mapper;

import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

import com.hand.dimension.hqm_dimension_order.dto.DimensionOrder;

public interface DimensionOrderMapper extends Mapper<DimensionOrder>{

	/**
	 * 
	 * @description 普通查询
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param dto
	 * @return
	 */
	List<DimensionOrder> reselect(DimensionOrder dto);
	/**
	 * 
	 * @description 最大流水号查询
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param dto
	 * @return
	 */
	List<DimensionOrder> selectMaxNumber(DimensionOrder dto);
}