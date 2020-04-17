package com.hand.hqm.hqm_msa_plan_line.service;

import java.util.Date;
import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_msa_plan_line.dto.MsaPlanLine;

public interface IMsaPlanLineService extends IBaseService<MsaPlanLine>, ProxySelf<IMsaPlanLineService>{
	
	/**
	 * 更改分析结论
	 * @param requestContext
	 * @param dto
	 */
	void updateResult(IRequest requestContext,MsaPlanLine dto);
	/**
	 * 更改分析日期
	 * @param requestContext
	 * @param dto
	 */
	List<MsaPlanLine> updateAnalyzeData(IRequest requestContext,MsaPlanLine dto);
}