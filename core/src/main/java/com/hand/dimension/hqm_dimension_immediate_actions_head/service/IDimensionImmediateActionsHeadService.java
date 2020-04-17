package com.hand.dimension.hqm_dimension_immediate_actions_head.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

import com.hand.dimension.hqm_dimension_immediate_actions_head.dto.DimensionImmediateActionsHead;
import com.hand.dimension.hqm_dimension_improving_actions.dto.DimensionImprovingActions;

public interface IDimensionImmediateActionsHeadService extends IBaseService<DimensionImmediateActionsHead>, ProxySelf<IDimensionImmediateActionsHeadService>{

	/**
	 * 
	 * @description 保存单个
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<DimensionImmediateActionsHead> saveOne(IRequest requestContext, DimensionImmediateActionsHead dto);

	/**
	 * 
	 * @description 删除单个
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<DimensionImmediateActionsHead> deleteOne(IRequest requestContext, DimensionImmediateActionsHead dto);

	/**
	 * 
	 * @description 步骤提交
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param requestContext
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	ResponseData commit(IRequest requestContext, DimensionImmediateActionsHead dto) throws Exception;

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
	List<DimensionImmediateActionsHead> reBatchUpdate(IRequest requestCtx, List<DimensionImmediateActionsHead> dto) throws Exception;

	/**
	 * 
	 * @description 基础查询
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<DimensionImmediateActionsHead> reSelect(IRequest requestContext, DimensionImmediateActionsHead dto, int page, int pageSize);

	/**
	 * @Description: 批量修改
	 * @param dto
	 */
	void batchUpdateById(List<DimensionImmediateActionsHead> dto);

	/**
	 * @Description: 查询删除历史
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<DimensionImmediateActionsHead> reSelectDelete(IRequest requestContext, DimensionImmediateActionsHead dto, int page, int pageSize);


}