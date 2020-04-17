package com.hand.hqm.hqm_measure_tool_repair.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_measure_tool_repair.dto.MeasureToolRepair;

public interface IMeasureToolRepairService extends IBaseService<MeasureToolRepair>, ProxySelf<IMeasureToolRepairService>{

	/**
	 * @Description:
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<MeasureToolRepair> queryByCondition(IRequest requestContext, MeasureToolRepair dto, int page, int pageSize);

	/**
	 * @Description:
	 * @param requestCtx
	 * @param dto
	 * @return
	 */
	List<MeasureToolRepair> add(IRequest requestCtx, MeasureToolRepair dto);

	/**
	 * @Description:
	 * @param requestCtx
	 * @param dto
	 * @return
	 */
	List<MeasureToolRepair> confirmRepair(IRequest requestCtx, MeasureToolRepair dto);

}