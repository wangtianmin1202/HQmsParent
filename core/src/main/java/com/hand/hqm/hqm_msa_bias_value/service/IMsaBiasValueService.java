package com.hand.hqm.hqm_msa_bias_value.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_msa_bias_value.dto.MsaBiasValue;
public interface IMsaBiasValueService extends IBaseService<MsaBiasValue>, ProxySelf<IMsaBiasValueService>{
	
	/**
	 * 偏倚分析数据录入查询界面
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<MsaBiasValue> query(IRequest requestContext,MsaBiasValue dto);
	/**
	 * 偏倚分析数据录入  保存
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<MsaBiasValue> submit(IRequest requestContext, List<MsaBiasValue> dto);
	/**
	 * 导入
	 * @param request
	 * @param requestContext
	 * @throws Exception
	 */
	List<MsaBiasValue> excelImport(HttpServletRequest request, IRequest requestContext) throws Exception;
	/**
	 * 偏倚  删除
	 * @param requestContext
	 * @param dto
	 */
	void removeByMsaPlanId(IRequest requestContext,MsaBiasValue dto);
}