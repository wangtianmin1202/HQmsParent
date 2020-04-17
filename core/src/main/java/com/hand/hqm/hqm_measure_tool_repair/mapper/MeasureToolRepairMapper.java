package com.hand.hqm.hqm_measure_tool_repair.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_measure_tool_repair.dto.MeasureToolRepair;

public interface MeasureToolRepairMapper extends Mapper<MeasureToolRepair>{

	/**
	 * @Description:
	 * @param dto
	 * @return
	 */
	List<MeasureToolRepair> queryByCondition(MeasureToolRepair dto);

}