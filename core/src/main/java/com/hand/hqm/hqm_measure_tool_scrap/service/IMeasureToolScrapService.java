package com.hand.hqm.hqm_measure_tool_scrap.service;

import java.util.List;

import com.hand.hap.activiti.dto.TaskActionRequestExt;
import com.hand.hap.activiti.exception.TaskActionException;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_measure_tool_scrap.dto.MeasureToolScrap;
import com.hand.wfl.util.WflTask;

public interface IMeasureToolScrapService extends IBaseService<MeasureToolScrap>, ProxySelf<IMeasureToolScrapService>{

	/**
	 * @Description:
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<MeasureToolScrap> queryByCondition(IRequest requestContext, MeasureToolScrap dto, int page, int pageSize);

	/**
	 * @Description:
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<MeasureToolScrap> add(IRequest requestContext, MeasureToolScrap dto);

	/**
	 * @Description:
	 * @param requestCtx
	 * @param dto
	 */
	void createProcessInstance(IRequest requestCtx, MeasureToolScrap dto);

	/**
	 * @Description:
	 * @param requestCtx
	 * @param list
	 * @param actionRequest
	 * @param processInstanceId
	 * @param flowNum
	 * @return
	 */
	ResponseData approveProcess(IRequest requestCtx, MeasureToolScrap dto, TaskActionRequestExt actionRequest,
			String processInstanceId, Integer flowNum) throws TaskActionException;

	/**
	 * @Description:
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<WflTask> queryTask(IRequest requestContext, String processInstanceId);

}