package com.hand.dimension.hqm_dimension_congratulate_team.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

import com.hand.dimension.hqm_dimension_congratulate_team.dto.DimensionCongratulateTeam;
import com.hand.dimension.hqm_dimension_prevention_actions.dto.DimensionPreventionActions;

public interface IDimensionCongratulateTeamService extends IBaseService<DimensionCongratulateTeam>, ProxySelf<IDimensionCongratulateTeamService>{
	
	
	/**
	 * 
	 * @description 总结 按照团队组建产生数据
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param requestContext
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	int createByTeam(IRequest requestContext,Float orderId) throws Exception;
	
	/**
	 * 
	 * @description 提交8d的团队庆祝步骤
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param requestContext
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public ResponseData commit(IRequest requestContext, DimensionCongratulateTeam dto) throws Exception;
	
	/**
	 * 
	 * @description 自定义结果集合查询
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<DimensionCongratulateTeam> reSelect(IRequest requestContext, DimensionCongratulateTeam dto, int page, int pageSize);
	
	
}