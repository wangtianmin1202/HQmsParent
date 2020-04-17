package com.hand.hqm.hqm_measure_tool_use.service;

import java.util.List;

import com.hand.hap.activiti.dto.TaskActionRequestExt;
import com.hand.hap.activiti.exception.TaskActionException;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_measure_tool_use.dto.MeasureToolUse;

public interface IMeasureToolUseService extends IBaseService<MeasureToolUse>, ProxySelf<IMeasureToolUseService>{

	/**
	 * @Description:
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<MeasureToolUse> queryByCondition(IRequest requestContext, MeasureToolUse dto, int page, int pageSize);

	/**
	 * @Description:
	 * @param requestCtx
	 * @param dto
	 * @return
	 */
	List<MeasureToolUse> add(IRequest requestCtx, MeasureToolUse dto);

	/**
	 * @Description:
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<MeasureToolUse> confirmReturn(IRequest requestContext, MeasureToolUse dto);

	/**
	 * @Description:
	 * @param requestCtx
	 * @param dto
	 */
	void createProcessInstance(IRequest requestCtx, MeasureToolUse dto);

	/**
	 * @Description:
	 * @param requestCtx
	 * @param list
	 * @param actionRequest
	 * @param processInstanceId
	 * @param flowNum
	 * @return
	 * @throws TaskActionException 
	 */
	ResponseData approveProcess(IRequest requestCtx, MeasureToolUse dto, TaskActionRequestExt actionRequest,
			String processInstanceId, Integer flowNum) throws TaskActionException;

}