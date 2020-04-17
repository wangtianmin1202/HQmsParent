package com.hand.dimension.hqm_dimension_initiated_actions.mapper;

import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

import com.hand.dimension.hqm_dimension_initiated_actions.dto.DimensionInitiatedActions;

public interface DimensionInitiatedActionsMapper extends Mapper<DimensionInitiatedActions>{

	/**
	 * 
	 * @description 基础查询
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param dto
	 * @return
	 */
	List<DimensionInitiatedActions> reSelect(DimensionInitiatedActions dto);

}