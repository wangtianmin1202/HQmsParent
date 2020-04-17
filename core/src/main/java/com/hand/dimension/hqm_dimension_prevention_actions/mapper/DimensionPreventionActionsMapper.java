package com.hand.dimension.hqm_dimension_prevention_actions.mapper;

import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

import com.hand.dimension.hqm_dimension_prevention_actions.dto.DimensionPreventionActions;

public interface DimensionPreventionActionsMapper extends Mapper<DimensionPreventionActions>{

	/**
	 * 
	 * @description 查询
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param dto
	 * @return
	 */
	List<DimensionPreventionActions> reSelect(DimensionPreventionActions dto);
	/**
	 * 
	 * @description 删除查询
	 * @author magicor
	 * @date 2019年11月21日 
	 * @param dto
	 * @return
	 */
	List<DimensionPreventionActions> reSelectDelete(DimensionPreventionActions dto);
}