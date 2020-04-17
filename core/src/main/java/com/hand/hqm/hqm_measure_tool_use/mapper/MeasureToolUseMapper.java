package com.hand.hqm.hqm_measure_tool_use.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_measure_tool_use.dto.MeasureToolUse;

public interface MeasureToolUseMapper extends Mapper<MeasureToolUse>{


	/**
	 * @Description:
	 * @param dto
	 * @return
	 */
	List<MeasureToolUse> queryByCondition(MeasureToolUse dto);
	
	/**
	 * @Description:
	 * @param dto
	 * @return
	 */
	List<MeasureToolUse> queryByProcessInstId(MeasureToolUse dto);

}