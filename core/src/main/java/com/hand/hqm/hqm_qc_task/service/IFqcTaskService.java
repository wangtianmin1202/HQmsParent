package com.hand.hqm.hqm_qc_task.service;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hap.util.SoapPostUtil.Response;
import com.hand.hap.util.SoapPostUtil.ResponseSap;
import com.hand.hap.webservice.ws.idto.FqcOutLineInspection;
import com.hand.hap.webservice.ws.idto.RecallTestInfo;
import com.hand.hqm.hqm_qc_task.dto.FqcTask;

public interface IFqcTaskService extends IBaseService<FqcTask>, ProxySelf<IFqcTaskService>{
	/**
	 * @description 接收wms接口传入的
	 * @author tianmin.wang
	 * @date 2019年11月18日 
	 * @return
	 */
	Response transferFqcTask();

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月19日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<FqcTask> reSelect(IRequest requestContext, FqcTask dto, int page, int pageSize);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年1月7日 
	 * @param dto
	 * @param request
	 * @throws Exception 
	 */
	void createFqc(List<FqcTask> dto, HttpServletRequest request) throws Exception;
	
	/**
	 * @description WMS-返修品/成品召回FQC报验申请
	 * @author kai.li
	 * @date 2020年2月25日 
	 * @param dto
	 */
	ResponseSap fqcRecallRetest(RecallTestInfo dto, List<FqcTask> fqcTaskDto) throws Exception;

	/**
	 * @description fqc下线报检申请接口
	 * @author tianmin.wang
	 * @date 2020年2月28日 
	 * @param foi
	 * @return
	 * @throws ParseException 
	 * @throws Exception 
	 */
	ResponseSap fqcOutLineInspection(FqcOutLineInspection foi) throws ParseException, Exception;

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月19日 
	 * @param dto
	 * @param request
	 * @throws Exception
	 */
	void createFqc(FqcTask dto, HttpServletRequest request) throws Exception;
}