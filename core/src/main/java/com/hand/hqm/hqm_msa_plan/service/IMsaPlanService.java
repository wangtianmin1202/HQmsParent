package com.hand.hqm.hqm_msa_plan.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_msa_plan.dto.MsaPlan;

public interface IMsaPlanService extends IBaseService<MsaPlan>, ProxySelf<IMsaPlanService>{
	
	List<MsaPlan> query(IRequest requestContext,MsaPlan dto ,int page,int PageSize);
	/**
	 * 保存
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<MsaPlan> update(IRequest requestContext,List<MsaPlan> dto);
	/**
	 * msa计划制定 取消
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<MsaPlan> cancel(IRequest requestContext,List<MsaPlan> dto);
	/**
	 * msa计划执行 完成
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<MsaPlan> complete(IRequest requestContext,List<MsaPlan> dto);
}	