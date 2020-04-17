package com.hand.hqm.hqm_qc_task.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hap.util.SoapPostUtil.Response;
import com.hand.hap.util.SoapPostUtil.ResponseSap;
import com.hand.hap.webservice.ws.idto.TaskInComeInspect;
import com.hand.hqm.hqm_qc_task.dto.IqcTask;

public interface IIqcTaskService extends IBaseService<IqcTask>, ProxySelf<IIqcTaskService>{

	/**
	 * @description 接收wms接口传入的
	 * @author tianmin.wang
	 * @date 2019年11月18日 
	 * @return
	 */
	Response transferIqcTask();

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月18日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<IqcTask> reSelect(IRequest requestContext, IqcTask dto, int page, int pageSize);

	/**
	 * @description 生成IQC报告
	 * @author tianmin.wang
	 * @date 2020年1月7日 
	 * @param dto
	 * @param request 
	 * @return
	 */
	void createIqc(List<IqcTask> dto, HttpServletRequest request);
	
	/**
	 * @description 报验接口生成IQC报告
	 * @author tianmin.wang
	 * @date 2020年1月7日 
	 * @param dto
	 * @param request 
	 * @return
	 */
	void createIqcInterface(List<IqcTask> dto, HttpServletRequest request);

	/**
	 * @description WMS-IQC来料报验申请
	 * @author kai.li
	 * @date 2020年2月24日 
	 * @param dto
	 * @return
	 */
	ResponseSap inspectWmsIncome(TaskInComeInspect dto, List<IqcTask> iqcTaskDto) throws Exception;

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月20日 
	 * @param dto
	 * @param request
	 */
	void createIqc(IqcTask dto, HttpServletRequest request);
}