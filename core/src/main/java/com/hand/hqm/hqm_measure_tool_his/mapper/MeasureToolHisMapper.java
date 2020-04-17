package com.hand.hqm.hqm_measure_tool_his.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_measure_tool_his.dto.MeasureToolHis;
import com.hand.hqm.hqm_measure_tool_his.dto.MeasureToolHisVO;

public interface MeasureToolHisMapper extends Mapper<MeasureToolHis>{
	/**
	 * 查询
	 * @param measureToolHis
	 * @return
	 */
	List<MeasureToolHis> query(MeasureToolHis measureToolHis);

	List<MeasureToolHisVO> queryCheckType(MeasureToolHis measureToolHis);

	List<MeasureToolHis> queryCheckTypeGrid(MeasureToolHis measureToolHis);

	List<MeasureToolHisVO> queryDepartmentUsage(MeasureToolHis measureToolHis);

	List<MeasureToolHis> queryDepartmentUsageGrid(MeasureToolHis measureToolHis);
}