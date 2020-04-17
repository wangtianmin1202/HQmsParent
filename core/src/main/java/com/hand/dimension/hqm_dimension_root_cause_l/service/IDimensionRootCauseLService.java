package com.hand.dimension.hqm_dimension_root_cause_l.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.dimension.hqm_dimension_prevention_actions.dto.DimensionPreventionActions;
import com.hand.dimension.hqm_dimension_root_cause_l.dto.DimensionRootCauseL;

public interface IDimensionRootCauseLService extends IBaseService<DimensionRootCauseL>, ProxySelf<IDimensionRootCauseLService>{

	/**
	 * 
	 * @description 查询
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<DimensionRootCauseL> reSelect(IRequest requestContext, DimensionRootCauseL dto, int page, int pageSize);
	
	/**
	 * 
	 * @description 批量更新
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param requestCtx
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	List<DimensionRootCauseL> reBatchUpdate(IRequest requestCtx, List<DimensionRootCauseL> dto) throws Exception;
	/**
	 * @Description:批量更新
	 * @param request
	 * @param dto
	 */
	void batchUpdateById(HttpServletRequest request, List<DimensionRootCauseL> dto);

}