package com.hand.dimension.hqm_dimension_root_cause_l.mapper;

import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

import com.hand.dimension.hqm_dimension_root_cause_l.dto.DimensionRootCauseL;

public interface DimensionRootCauseLMapper extends Mapper<DimensionRootCauseL>{

	List<DimensionRootCauseL> reSelect(DimensionRootCauseL dto);
	
	List<DimensionRootCauseL> reSelectDelete(DimensionRootCauseL dto);
}