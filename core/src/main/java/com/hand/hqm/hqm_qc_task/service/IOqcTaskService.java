package com.hand.hqm.hqm_qc_task.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hap.util.SoapPostUtil.ResponseSap;
import com.hand.hap.webservice.ws.idto.ItemStock;
import com.hand.hqm.hqm_qc_task.dto.OqcTask;

public interface IOqcTaskService extends IBaseService<OqcTask>, ProxySelf<IOqcTaskService>{

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年1月8日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<OqcTask> reSelect(IRequest requestContext, OqcTask dto, int page, int pageSize);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年1月8日 
	 * @param request
	 * @param dto
	 * @throws Exception 
	 */
	void createOqc(HttpServletRequest request, List<OqcTask> dto) throws Exception;

	/**
	 * @description WMS-OQC库存超期预警
	 * @author kai.li
	 * @date 2020年2月25日 
	 * @param dto
	 * @return
	 */
	ResponseSap ItemStockRetest(ItemStock dto) throws Exception;
	
}