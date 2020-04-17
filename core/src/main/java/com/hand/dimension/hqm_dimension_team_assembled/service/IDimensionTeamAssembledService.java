package com.hand.dimension.hqm_dimension_team_assembled.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_sample_account_after.dto.SampleAccountAfter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.dimension.hqm_dimension_team_assembled.dto.DimensionTeamAssembled;

public interface IDimensionTeamAssembledService extends IBaseService<DimensionTeamAssembled>, ProxySelf<IDimensionTeamAssembledService>{

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
	List<DimensionTeamAssembled> myselect(IRequest requestContext, DimensionTeamAssembled dto, int page, int pageSize);

	/**
	 * 
	 * @description 多选界面数据查询
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<DimensionTeamAssembled> selectMulLov(IRequest requestContext, DimensionTeamAssembled dto, int page, int pageSize);

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
	List<DimensionTeamAssembled> batchUpdateRe(IRequest requestCtx, List<DimensionTeamAssembled> dto) throws Exception;

	/**
	 * 
	 * @description 提交
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param requestCtx
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	ResponseData commit(IRequest requestCtx, List<DimensionTeamAssembled> dto) throws Exception;
	
	/**
	 * 
	 * @description 保存成员
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param dto
	 * @param requestCtx
	 * @param request
	 * @return
	 */
	ResponseData saveMenber(DimensionTeamAssembled dto,IRequest requestCtx, HttpServletRequest request);

}