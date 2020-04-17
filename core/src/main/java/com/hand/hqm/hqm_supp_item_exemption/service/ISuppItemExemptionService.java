package com.hand.hqm.hqm_supp_item_exemption.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_asl_iqc_control.dto.AslIqcControl;
import com.hand.hqm.hqm_supp_item_exemption.dto.SuppItemExemption;

public interface ISuppItemExemptionService extends IBaseService<SuppItemExemption>, ProxySelf<ISuppItemExemptionService>{
	/**
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月6日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<SuppItemExemption>  myselect(IRequest requestContext,SuppItemExemption dto,int page, int pageSize);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月6日 
	 * @param requestCtx
	 * @param dto
	 * @return
	 */
	List<SuppItemExemption> reBatchUpdate(IRequest requestCtx, List<SuppItemExemption> dto);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月6日 
	 * @param request
	 * @param t
	 * @return
	 */
	SuppItemExemption insertSelectiveRecord(IRequest request, SuppItemExemption t);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月6日 
	 * @param request
	 * @param t
	 * @return
	 */
	SuppItemExemption updateByPrimaryKeySelectiveRecord(IRequest request, SuppItemExemption t);

}