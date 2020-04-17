package com.hand.dimension.hqm_dimension_immediate_actions_line.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

import com.hand.dimension.hqm_dimension_immediate_actions_line.dto.DimensionImmediateActionsLine;

public interface IDimensionImmediateActionsLineService extends IBaseService<DimensionImmediateActionsLine>, ProxySelf<IDimensionImmediateActionsLineService>{

	/**
	 * 
	 * @description 查询 无分页
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<DimensionImmediateActionsLine> reSelect(IRequest requestContext, DimensionImmediateActionsLine dto, int page, int pageSize);

}