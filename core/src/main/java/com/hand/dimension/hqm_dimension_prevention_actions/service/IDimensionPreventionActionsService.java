package com.hand.dimension.hqm_dimension_prevention_actions.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

import com.hand.dimension.hqm_dimension_prevention_actions.dto.DimensionPreventionActions;

public interface IDimensionPreventionActionsService extends IBaseService<DimensionPreventionActions>, ProxySelf<IDimensionPreventionActionsService>{

	/**
	 * 
	 * @description 重写查询
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<DimensionPreventionActions> reSelect(IRequest requestContext, DimensionPreventionActions dto, int page, int pageSize);

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
	ResponseData commit(IRequest requestContext, DimensionPreventionActions dto) throws Exception;

	/**
	 * 
	 * @description 批量发布
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param requestCtx
	 * @param dto
	 * @return
	 */
	List<DimensionPreventionActions> batchIssue(IRequest requestCtx, List<DimensionPreventionActions> dto);

	/**
	 * 
	 * @description 批量更细
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param requestCtx
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	List<DimensionPreventionActions> reBatchUpdate(IRequest requestCtx, List<DimensionPreventionActions> dto) throws Exception;
	/**
	 * @Description:批量删除
	 * @param dto
	 */
	void batchUpdateById(List<DimensionPreventionActions> dto);

	/**
	 * @Description:查询删除数据
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<DimensionPreventionActions> selectDelete(IRequest requestContext, DimensionPreventionActions dto, int page, int pageSize);

}