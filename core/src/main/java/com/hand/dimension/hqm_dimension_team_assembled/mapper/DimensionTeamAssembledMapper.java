package com.hand.dimension.hqm_dimension_team_assembled.mapper;

import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hand.dimension.hqm_dimension_team_assembled.dto.DimensionTeamAssembled;

public interface DimensionTeamAssembledMapper extends Mapper<DimensionTeamAssembled>{

	List<DimensionTeamAssembled> mulLovSelect(DimensionTeamAssembled dto);

	List<DimensionTeamAssembled> myselect(DimensionTeamAssembled dto);
	/**
	 * @Description: 判断成员总数
	 * @param orderId
	 * @param memberId
	 */
	Integer queryMemberTotalNum(@Param("orderId") Float orderId,@Param("memberId") Float memberId);
}