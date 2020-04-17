package com.hand.hqm.hqm_msa_grr_value.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_msa_grr_value.dto.MsaGrrValue;

public interface IMsaGrrValueService extends IBaseService<MsaGrrValue>, ProxySelf<IMsaGrrValueService>{
	
	/**
	 * GRR分析数据录入查询界面
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<MsaGrrValue> query(IRequest requestContext, MsaGrrValue dto);
	/**
	 * GRR分析数据录入界面  保存
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<MsaGrrValue> submit(IRequest requestContext, List<MsaGrrValue> dto);
	/**
	 * 求f分布
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	Double finvs(IRequest requestContext, MsaGrrValue dto);
	/**
	 * 导入
	 * @param request
	 * @param requestContext
	 * @throws Exception
	 */
	List<MsaGrrValue> excelImport(HttpServletRequest request, IRequest requestContext) throws Exception;
	/**
	 * grr分析 删除
	 * @param requestContext
	 * @param dto
	 */
	void removeByMsaPlanId(IRequest requestContext, MsaGrrValue dto);
}