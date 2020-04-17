package com.hand.dimension.hqm_dimension_improving_actions.mapper;

import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

import com.hand.dimension.hqm_dimension_improving_actions.dto.DimensionImprovingActions;

public interface DimensionImprovingActionsMapper extends Mapper<DimensionImprovingActions>{

	/**
	 * 
	 * @description 查询
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param dto
	 * @return
	 */
	List<DimensionImprovingActions> reSelect(DimensionImprovingActions dto);
	/**
	 * 
	 * @description 删除查询
	 * @author Magicor
	 * @date 2019年11月21日 
	 * @param dto
	 * @return
	 */
	List<DimensionImprovingActions> reSelectDelete(DimensionImprovingActions dto);
}