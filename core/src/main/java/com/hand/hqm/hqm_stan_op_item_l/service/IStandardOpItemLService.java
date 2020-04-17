package com.hand.hqm.hqm_stan_op_item_l.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_iqc_inspection_template_l.dto.IqcInspectionTemplateL;
import com.hand.hqm.hqm_stan_op_item_l.dto.StandardOpItemL;
import com.hand.hqm.hqm_standard_op_ins_l.dto.StandardOpInspectionL;

public interface IStandardOpItemLService extends IBaseService<StandardOpItemL>, ProxySelf<IStandardOpItemLService>{
	 List<StandardOpItemL>  myselect(IRequest requestContext,StandardOpItemL dto,int page, int pageSize); 
	 List<StandardOpItemL> saveHeadLine(IRequest requestContext, List<StandardOpItemL> dto);
	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月10日 
	 * @param request
	 * @param list
	 * @return
	 */
	List<StandardOpItemL> reBatchUpdate(IRequest request, List<StandardOpItemL> list);
	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月10日 
	 * @param request
	 * @param t
	 * @param eventHId
	 * @return
	 */
	StandardOpItemL updateByPrimaryKeySelectiveRecord(IRequest request, StandardOpItemL t, Float eventHId);
	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月10日 
	 * @param request
	 * @param t
	 * @param eventHId
	 * @return
	 */
	StandardOpItemL insertSelectiveRecord(IRequest request, StandardOpItemL t, Float eventHId);
	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月13日 
	 * @param request
	 * @param t
	 * @param eventHId
	 * @return
	 */
	StandardOpItemL deleteByPrimaryKeyRecoed(IRequest request, StandardOpItemL t, Float eventHId);
	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月13日 
	 * @param requestCtx
	 * @param dto
	 */
	int reBatchDelete(IRequest requestCtx, List<StandardOpItemL> dto);


}