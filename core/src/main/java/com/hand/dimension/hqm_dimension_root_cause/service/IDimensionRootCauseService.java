package com.hand.dimension.hqm_dimension_root_cause.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

import com.hand.dimension.hqm_dimension_root_cause.dto.DimensionRootCause;

public interface IDimensionRootCauseService extends IBaseService<DimensionRootCause>, ProxySelf<IDimensionRootCauseService>{

	
	/**
	 * 
	 * @description 单值保存
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param requestContext
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	List<DimensionRootCause> saveOne(IRequest requestContext, DimensionRootCause dto) throws Exception;

	/**
	 * 
	 * @description 提交操作
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param requestContext
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	ResponseData commit(IRequest requestContext, DimensionRootCause dto) throws Exception;

}