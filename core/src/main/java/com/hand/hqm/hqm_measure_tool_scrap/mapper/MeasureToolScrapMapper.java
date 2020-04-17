package com.hand.hqm.hqm_measure_tool_scrap.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_measure_tool_scrap.dto.MeasureToolScrap;
import com.hand.wfl.util.WflTask;
import com.hand.wfl.util.WflVariable;

public interface MeasureToolScrapMapper extends Mapper<MeasureToolScrap>{


	/**
	 * @Description:
	 * @param dto
	 */
	List<MeasureToolScrap> queryByCondition(MeasureToolScrap dto);
	
	List<WflTask> queryTask(@Param("processInstanceId") String processInstanceId);

	/**
	 * @Description:
	 * @param processInstanceId
	 * @return
	 */
	List<WflVariable> queryVariable(@Param("processInstanceId") String processInstanceId);

}