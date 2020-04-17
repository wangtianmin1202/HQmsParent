package com.hand.dimension.hqm_dimension_improving_actions.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

import com.hand.dimension.hqm_dimension_improving_actions.dto.DimensionImprovingActions;

public interface IDimensionImprovingActionsService extends IBaseService<DimensionImprovingActions>, ProxySelf<IDimensionImprovingActionsService>{

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
	List<DimensionImprovingActions> reSelect(IRequest requestContext, DimensionImprovingActions dto, int page, int pageSize);

	
	/**
	 * 
	 * @description 提交
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param requestContext
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	ResponseData commit(IRequest requestContext, DimensionImprovingActions dto) throws Exception;

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
	List<DimensionImprovingActions> reBatchUpdate(IRequest requestCtx, List<DimensionImprovingActions> dto) throws Exception;


	/**
	 * @Description:批量删除
	 * @param dto
	 */
	void batchUpdateById(List<DimensionImprovingActions> dto);


	/**
	 * @Description: 查询删除数据
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<DimensionImprovingActions> reSelectDelete(IRequest requestContext, DimensionImprovingActions dto, int page, int pageSize);

}