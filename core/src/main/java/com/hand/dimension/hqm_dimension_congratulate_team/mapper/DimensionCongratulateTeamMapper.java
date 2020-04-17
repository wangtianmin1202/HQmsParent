package com.hand.dimension.hqm_dimension_congratulate_team.mapper;

import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

import com.hand.dimension.hqm_dimension_congratulate_team.dto.DimensionCongratulateTeam;

public interface DimensionCongratulateTeamMapper extends Mapper<DimensionCongratulateTeam>{

	/**
	 * 
	 * @description 普通查询
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param dto
	 * @return
	 */
	List<DimensionCongratulateTeam> reSelect(DimensionCongratulateTeam dto);

}