package com.hand.hqm.hqm_measure_tool.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_measure_tool.dto.MeasureTool;
import com.hand.hqm.hqm_measure_tool.dto.MeasureToolVO;

public interface IMeasureToolService extends IBaseService<MeasureTool>, ProxySelf<IMeasureToolService>{
	
	/**
	 * 量具台账查询
	 * @param requestContext 请求上下文
	 * @param measureTool	量具信息
	 * @param page 页码
	 * @param pageSize 页大小
	 * @return 量具集合
	 */
	List<MeasureTool> query(IRequest requestContext,MeasureTool measureTool, int page, int pageSize);
	/**
	 * 量具领用，报废，维修查询
	 * @param requestContext 请求上下文
	 * @param measureTool 量具信息
	 * @param page 页码
	 * @param pageSize 页大小
	 * @return	量具集合
	 */
	List<MeasureTool> queryByCode(IRequest requestContext,MeasureTool measureTool, int page, int pageSize);
	/**
	 * 新建量具台账
	 * @param requestContext 请求上下文
	 * @param measureTool 量具信息
	 * @return
	 */
	void saveData(IRequest requestContext,MeasureTool measureTool);
	/**
	 * 归还
	 * @param requestContext 请求上下文
	 * @param dto 量具集合
	 * @return	量具集合
	 */
	List<MeasureTool> returnData(IRequest requestContext,List<MeasureTool> dto);
	/**
	 * 报废
	 * @param requestContext 请求上下文
	 * @param dto 量具集合
	 * @return
	 */
	List<MeasureTool> scrap(IRequest requestContext,List<MeasureTool> dto);
	/**
	 * 校验结果
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<MeasureTool> checkResult(IRequest requestContext,List<MeasureTool> dto);
	/**
	 * 更新
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<MeasureTool> batchSave(IRequest requestContext,List<MeasureTool> dto);
	
	/**
	 * 量具MSA周期校验查詢量具信息
	 * @param measureTool
	 * @return
	 */
	List<MeasureTool> jobSelect(MeasureTool measureTool);
	/**
	 * 量具台账LOV
	 * @param measureTool
	 * @return
	 */
	List<MeasureTool> MeasureToolLov(MeasureTool measureTool);
	/**
	 *  获取领用部门、领用人
	 * @param measureTool
	 * @return
	 */
	List<MeasureTool> queryUnit(MeasureTool measureTool);
	/**
	 * 领用
	 * @param requestContext
	 * @param dto
	 */
	void forUse(IRequest requestContext,List<MeasureTool> dto);
	/**
	 * 维修
	 * @param requestContext
	 * @param dto
	 */
	void repair(IRequest requestContext,MeasureTool dto);
	/**
	 * 编辑
	 * @param requestContext
	 * @param measureTool
	 */
	void updateData(IRequest requestContext,MeasureTool measureTool);

	/**
	 *量具校验清单获取量具信息
	 * @param measureTool
	 * @return
	 */
	List<MeasureTool> metCheckPlanJob(MeasureTool measureTool);

	
	List<MeasureTool> statisticsSelect(IRequest requestContext, MeasureTool dto, int page, int pageSize);
	
	/**
	 * 编辑MSA
	 * @param requestContext
	 * @param measureTool
	 */
	void saveMsa(IRequest requestContext,MeasureTool measureTool);
	
	/**
	 * 查询报表
	 * @param requestContext
	 * @param measureTool
	 */
	List<MeasureToolVO> queryReport(IRequest requestContext, MeasureTool dto);
	/**
	 * 根据量具类型查询报表
	 * @param requestContext
	 * @param measureTool
	 */
	List<MeasureTool> queryByToolType(IRequest requestContext, MeasureTool dto, int page, int pageSize);
	
	List<MeasureToolVO> queryMsaReport(IRequest requestContext, MeasureTool dto);
	
	List<MeasureTool> queryMsaGridReport(IRequest requestContext, MeasureTool measureTool, int page, int pageSize);

}