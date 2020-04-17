package com.hand.hqm.hqm_program_sku_rel.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_program_sku_rel.dto.ProgramSkuRel;

public interface IProgramSkuRelService extends IBaseService<ProgramSkuRel>, ProxySelf<IProgramSkuRelService>{

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年1月9日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<ProgramSkuRel> reSelect(IRequest requestContext, ProgramSkuRel dto, int page, int pageSize);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年1月9日 
	 * @param requestCtx
	 * @param dto
	 * @return
	 */
	List<ProgramSkuRel> reBatchUpdate(IRequest requestCtx, List<ProgramSkuRel> dto);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月6日 
	 * @param request
	 * @param t
	 * @return
	 */
	ProgramSkuRel insertSelectiveRecord(IRequest request, ProgramSkuRel t);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月6日 
	 * @param request
	 * @param t
	 * @return
	 */
	ProgramSkuRel updateByPrimaryKeySelectiveRecord(IRequest request, ProgramSkuRel t);

}