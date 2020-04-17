package com.hand.dimension.hqm_dimension_immediate_actions_line.mapper;

import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

import com.hand.dimension.hqm_dimension_immediate_actions_line.dto.DimensionImmediateActionsLine;

public interface DimensionImmediateActionsLineMapper extends Mapper<DimensionImmediateActionsLine>{
	
	
	
	/**
	 * 
	 * @description 基础查询
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param dto
	 * @return
	 */
	List<DimensionImmediateActionsLine> reSelect(DimensionImmediateActionsLine dto);

}