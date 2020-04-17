package com.hand.hqm.hqm_measure_tool.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_measure_tool.dto.MeasureTool;
import com.hand.hqm.hqm_measure_tool.dto.MeasureToolVO;

public interface MeasureToolMapper extends Mapper<MeasureTool>{
	
	/**
	 * 量具台账LOV
	 * @param measureTool 量具信息
	 * @return 量具集合
	 */
	List<MeasureTool> MeasureToolLov(MeasureTool measureTool);
	/**
	 * 量具台账查询
	 * @param measureTool 量具信息
	 * @return 量具集合
	 */
	List<MeasureTool> query(MeasureTool measureTool);
	/**
	 * 量具领用查询
	 * @param measureTool	量具信息
	 * @return 量具集合
	 */
	List<MeasureTool> queryNeckband(MeasureTool measureTool);
	/**
	 * 量具报废查询
	 * @param measureTool	量具信息
	 * @return 量具集合
	 */
	List<MeasureTool> queryScrap(MeasureTool measureTool);
	/**
	 * 量具维修查询
	 * @param measureTool	量具信息
	 * @return 量具集合
	 */
	List<MeasureTool> queryMaintain(MeasureTool measureTool);
	/**
	 * 量具台账归还
	 * @param measureTool	量具信息
	 */
	void updatemeasureToolPositionStatus(MeasureTool measureTool);
	/**
	 * 量具MSA周期校验查詢量具信息
	 * @param measureTool	量具信息
	 * @return 量具集合
	 */
	List<MeasureTool> jobSelect(MeasureTool measureTool);
	/**
	 * 获取领用部门、领用人
	 * @param measureTool	量具信息
	 * @return 量具集合
	 */
	List<MeasureTool> queryUnit(MeasureTool measureTool);

	/**
	 * 量具校验清单获取量具信息
	 * @param measureTool	量具信息
	 * @return 量具集合
	 */
	List<MeasureTool> metCheckPlanJob(MeasureTool measureTool);

	List<MeasureTool> statisticsSelect(MeasureTool dto);
	
	List<MeasureToolVO> queryReport(MeasureTool measureTool);
	
	List<MeasureTool> queryByToolType(MeasureTool measureTool);
	
	List<MeasureToolVO> queryMsaReport(MeasureTool measureTool);
	
	List<MeasureTool> queryMsaGridReport(MeasureTool measureTool);

}