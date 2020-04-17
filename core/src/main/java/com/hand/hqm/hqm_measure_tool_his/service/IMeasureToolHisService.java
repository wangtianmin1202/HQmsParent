package com.hand.hqm.hqm_measure_tool_his.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_measure_tool_his.dto.MeasureToolHis;
import com.hand.hqm.hqm_measure_tool_his.dto.MeasureToolHisVO;

public interface IMeasureToolHisService extends IBaseService<MeasureToolHis>, ProxySelf<IMeasureToolHisService>{
	/**
	 * 查询
	 * @param requestContext 请求上下文
	 * @param measureToolHis 量具历史
	 * @param page 页码
	 * @param pageSize 页大小
	 * @return 结果集
	 */
	List<MeasureToolHis> query(IRequest requestContext,MeasureToolHis measureToolHis, int page, int pageSize);

	List<MeasureToolHisVO> queryCheckType(IRequest requestContext, MeasureToolHis measureToolHis);

	List<MeasureToolHis> queryCheckTypeGrid(IRequest requestContext, MeasureToolHis measureToolHis, int page, int pageSize);

	List<MeasureToolHisVO> queryDepartmentUsage(IRequest requestContext, MeasureToolHis measureToolHis);

	List<MeasureToolHis> queryDepartmentUsageGrid(IRequest requestContext, MeasureToolHis measureToolHis, int page, int pageSize);
}