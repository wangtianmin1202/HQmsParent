package com.hand.dimension.hqm_dimension_problem_description.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.dimension.hqm_dimension_problem_description.dto.DimensionProblemDescription;

public interface IDimensionProblemDescriptionService extends IBaseService<DimensionProblemDescription>, ProxySelf<IDimensionProblemDescriptionService>{

	/**
	 * 
	 * @description 查询
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param dto
	 * @return
	 */
	List<DimensionProblemDescription> reSelect(DimensionProblemDescription dto);

	/**
	 * 
	 * @description 保存明细
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param requestContext
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	List<DimensionProblemDescription> saveDetail(IRequest requestContext, DimensionProblemDescription dto) throws Exception;

	/**
	 * 
	 * @description 明细提交
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param requestContext
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	ResponseData commitDetail(IRequest requestContext, DimensionProblemDescription dto) throws Exception;

	/**
	 * 
	 * @description 文件上传
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param requestCtx
	 * @param request
	 * @return
	 */
	ResponseData fileUpload(IRequest requestCtx, HttpServletRequest request);

	/**
	 * 
	 * @description 相同物料的所有数据
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param dto
	 * @return
	 */
	List<DimensionProblemDescription> selectDimensionByItem(DimensionProblemDescription dto);
	
	/**
	 * 
	 * @description 相同nggroup组的所有数据
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param dto
	 * @return
	 */
	List<DimensionProblemDescription> selectDimensionByItemGroupCount(DimensionProblemDescription dto);

}