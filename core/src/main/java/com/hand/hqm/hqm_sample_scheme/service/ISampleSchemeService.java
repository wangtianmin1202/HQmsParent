package com.hand.hqm.hqm_sample_scheme.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_sample_scheme.dto.SampleScheme;

public interface ISampleSchemeService extends IBaseService<SampleScheme>, ProxySelf<ISampleSchemeService>{
	 /**
     * 提交
     * @param dto 操作数据集
     * @param result 结果参数
     * @param request 请求
     * @return 操作结果
     */
	List<SampleScheme> rebatchUpdate(IRequest requestCtx, List<SampleScheme> dto);
	/**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
	List<SampleScheme> reSelect(IRequest requestContext, SampleScheme dto, int page, int pageSize);

}