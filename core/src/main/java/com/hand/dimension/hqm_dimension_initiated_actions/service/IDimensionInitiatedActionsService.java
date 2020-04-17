package com.hand.dimension.hqm_dimension_initiated_actions.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.dimension.hqm_dimension_initiated_actions.dto.DimensionInitiatedActions;

public interface IDimensionInitiatedActionsService extends IBaseService<DimensionInitiatedActions>, ProxySelf<IDimensionInitiatedActionsService>{

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
	List<DimensionInitiatedActions> reSelect(IRequest requestContext, DimensionInitiatedActions dto, int page, int pageSize);

	/**
	 * 
	 * @description 文件上传保存至
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param requestCtx
	 * @param request
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	ResponseData fileUpload(IRequest requestCtx, HttpServletRequest request) throws IllegalStateException, IOException;

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
	ResponseData commit(IRequest requestContext, DimensionInitiatedActions dto) throws Exception;

	/**
	 * 
	 * @description 批量数据更新
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param requestCtx
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	List<DimensionInitiatedActions> reBatchUpdate(IRequest requestCtx, List<DimensionInitiatedActions> dto) throws Exception;

}