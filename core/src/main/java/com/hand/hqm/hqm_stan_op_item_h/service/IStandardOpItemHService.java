package com.hand.hqm.hqm_stan_op_item_h.service;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hcm.hcm_object_events.dto.ObjectEvents;
import com.hand.hqm.hqm_stan_op_item_h.dto.StandardOpItemH;

public interface IStandardOpItemHService extends IBaseService<StandardOpItemH>, ProxySelf<IStandardOpItemHService>{
	List<StandardOpItemH>  myselect(IRequest requestContext,StandardOpItemH dto,int page, int pageSize);

	int reBatchDelete(List<StandardOpItemH> dto);

	List<StandardOpItemH> save(IRequest requestContext, StandardOpItemH dto);

	void updateStatus(IRequest requestCtx, List<StandardOpItemH> dto); 
	
	/**
	 * @description PQC检验单模板导入
	 * @author kai.li
	 * @date 2020年2月28日 
	 * @param request
	 * @param requestContext
	 * @param inputStream
	 * @return
	 */
	ResponseData inputDataFromExcel(HttpServletRequest request, IRequest requestContext, InputStream inputStream)
			throws Exception;
	
	/**
	 * @description PQC检验单模板审核
	 * @author kai.li
	 * @date 2020年3月4日 
	 * @param requestCtx
	 * @param dto
	 * @return
	 */
	void audit(IRequest requestCtx, List<StandardOpItemH> dto);
	
	/**
	 * @description PQC检验单模板提交
	 * @author kai.li
	 * @date 2020年3月4日 
	 * @param requestCtx
	 * @param dto
	 * @return
	 */
	void commit(IRequest requestCtx, List<StandardOpItemH> dto);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月10日 
	 * @param request
	 * @param t
	 * @return
	 */
	StandardOpItemH insertSelectiveRecord(IRequest request, StandardOpItemH t);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月10日 
	 * @param request
	 * @param t
	 * @return
	 */
	StandardOpItemH updateByPrimaryKeySelectiveRecord(IRequest request, StandardOpItemH t);
	
	/**
	 * 审批时展示历史变更记录
	 * @author kai.li
	 * @param requestContext
	 * @param headId
	 * @param startTime
	 * @return
	 * @throws ParseException
	 */
	public List<ObjectEvents> getHisContentPqc(IRequest requestContext, Float headId, String startTime);
}