package com.hand.hqm.hqm_qc_task.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hap.util.SoapPostUtil;
import com.hand.hap.webservice.ws.idto.WorkOperationChange;
import com.hand.hqm.hqm_qc_task.dto.PqcTask;

public interface IPqcTaskService extends IBaseService<PqcTask>, ProxySelf<IPqcTaskService>{

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月27日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<PqcTask> reSelect(IRequest requestContext, PqcTask dto, int page, int pageSize);

	/**
	 * @description 生成报告
	 * @author tianmin.wang
	 * @date 2020年1月8日 
	 * @param request
	 * @param pqcTask
	 */
	String createPqc(HttpServletRequest request, PqcTask pqcTask);

	SoapPostUtil.ResponseSap workOperationChange(WorkOperationChange woc);
}