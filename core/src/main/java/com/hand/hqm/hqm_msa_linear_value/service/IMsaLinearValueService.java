package com.hand.hqm.hqm_msa_linear_value.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_msa_linear_value.dto.MsaLinearValue;

public interface IMsaLinearValueService extends IBaseService<MsaLinearValue>, ProxySelf<IMsaLinearValueService>{
	/**
	 * 线性分析录入界面  查询
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<MsaLinearValue> query(IRequest requestContext,MsaLinearValue dto);
	/**
	 * 线性分析录入界面 保存
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<MsaLinearValue> submit(IRequest requestContext, List<MsaLinearValue> dto);
	/**
	 * 导入
	 * @param request
	 * @param requestContext
	 * @throws Exception
	 */
	List<MsaLinearValue> excelImport(HttpServletRequest request, IRequest requestContext) throws Exception;
	/**
	 * 线性分析 删除
	 * @param requestContext
	 * @param dto
	 */
	void removeByMsaPlanId(IRequest requestContext,MsaLinearValue dto);
}