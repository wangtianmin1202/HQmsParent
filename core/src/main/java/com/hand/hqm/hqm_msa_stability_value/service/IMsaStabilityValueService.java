package com.hand.hqm.hqm_msa_stability_value.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_msa_stability_value.dto.MsaStabilityValue;

public interface IMsaStabilityValueService extends IBaseService<MsaStabilityValue>, ProxySelf<IMsaStabilityValueService>{
	
	/**
	 * 稳定性分析界面查询
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<MsaStabilityValue> query(IRequest requestContext,MsaStabilityValue dto);
	/**
	 * 稳定性分析保存
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<MsaStabilityValue> submit(IRequest requestContext, List<MsaStabilityValue> dto);
	/**
	 * 导入
	 * @param request
	 * @param requestContext
	 * @throws Exception
	 */
	List<MsaStabilityValue> excelImport(HttpServletRequest request, IRequest requestContext) throws Exception;
	/**
	 * 删除
	 * @param requestContext
	 * @param dto
	 */
	void removeByMsaPlanId(IRequest requestContext,MsaStabilityValue dto);
}