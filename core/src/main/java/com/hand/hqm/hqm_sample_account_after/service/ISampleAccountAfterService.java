package com.hand.hqm.hqm_sample_account_after.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_pfmea_level.dto.PfmeaLevel;
import com.hand.hqm.hqm_sample_account_after.dto.SampleAccountAfter;

public interface ISampleAccountAfterService extends IBaseService<SampleAccountAfter>, ProxySelf<ISampleAccountAfterService>{

	List<SampleAccountAfter> reSelect(IRequest requestContext, SampleAccountAfter dto, int page, int pageSize);

	List<SampleAccountAfter> createSave(IRequest requestContext, SampleAccountAfter dto);

	 ResponseData saveFx(SampleAccountAfter dto,IRequest requestCtx, HttpServletRequest request);

	/**
	 * @Description:
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<SampleAccountAfter> selectBf(IRequest requestContext, SampleAccountAfter dto, int page, int pageSize);

	/**
	 * @Description:用户校验
	 * @param requestContext
	 * @param userId
	 * @return
	 */
	ResponseData checkUserId(IRequest requestContext, Float userId);

	/**
	 * @Description: 编辑保存
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	ResponseData updateData(IRequest requestContext, SampleAccountAfter dto);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月2日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<SampleAccountAfter> selectLy(IRequest requestContext, SampleAccountAfter dto, int page, int pageSize);
	  
}