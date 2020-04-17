package com.hand.hqm.hqm_msa_consistency_value.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_msa_consistency_value.dto.MsaConsistencyValue;

public interface IMsaConsistencyValueService extends IBaseService<MsaConsistencyValue>, ProxySelf<IMsaConsistencyValueService>{
	/**
	 * 一致性分析数据录入 查询
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<MsaConsistencyValue> query(IRequest requestContext,MsaConsistencyValue dto);
	/**
	 * 一致性分析数据录入 保存
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<MsaConsistencyValue> submit(IRequest requestContext, List<MsaConsistencyValue> dto);
	/**
	 * 导入
	 * @param request
	 * @param requestContext
	 * @throws Exception
	 */
	List<MsaConsistencyValue> excelImport(HttpServletRequest request, IRequest requestContext) throws Exception;
	/**
	 * 一致性分析 删除
	 * @param requestContext
	 * @param dto
	 */
	void removeByMsaPlanId(IRequest requestContext,MsaConsistencyValue dto);
}