package com.hand.dimension.hqm_dimension_immediate_actions_head.mapper;

import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

import com.hand.dimension.hqm_dimension_immediate_actions_head.dto.DimensionImmediateActionsHead;

public interface DimensionImmediateActionsHeadMapper extends Mapper<DimensionImmediateActionsHead>{

	/**
	 * 
	 * @description 基础查询
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param dto
	 * @return
	 */
	List<DimensionImmediateActionsHead> reSelect(DimensionImmediateActionsHead dto);

	/**
	 * 
	 * @description 删除查询
	 * @author Magicor
	 * @date 2019年11月21日 
	 * @param dto
	 * @return
	 */
	List<DimensionImmediateActionsHead> reSelectDelete(DimensionImmediateActionsHead dto);
}