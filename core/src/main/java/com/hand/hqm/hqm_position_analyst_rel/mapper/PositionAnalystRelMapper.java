package com.hand.hqm.hqm_position_analyst_rel.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_position_analyst_rel.dto.PositionAnalystRel;

public interface PositionAnalystRelMapper extends Mapper<PositionAnalystRel>{
	/**
	 * 关键岗位与分析人关系维护  查询
	 * @param dto
	 * @return
	 */
	List<PositionAnalystRel> query(PositionAnalystRel dto);
}