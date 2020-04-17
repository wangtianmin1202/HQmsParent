package com.hand.hqm.hqm_sample_scrapped.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_sample.dto.Sample;
import com.hand.hqm.hqm_sample_account.dto.SampleAccount;
import com.hand.hqm.hqm_sample_scrapped.dto.SampleScrapped;

public interface ISampleScrappedService extends IBaseService<SampleScrapped>, ProxySelf<ISampleScrappedService>{
	/**
     * 样品新建
     * @param dto 操作数据集
     * @param result 结果参数
     * @param request 请求
     * @return 操作结果
     */
	 ResponseData addNew(SampleScrapped dto,IRequest requestCtx, HttpServletRequest request);
	 /**
	     * 提交
	     * @param dto 操作数据集
	     * @param result 结果参数
	     * @param request 请求
	     * @return 操作结果
	     */
	 ResponseData comfirm(IRequest requestContext, List<String> headList);
	 /**
	     * 查询
	     * @param dto 查询内容
	     * @param page 页码
	     * @param pageSize 页大小
	     * @param request 请求
	     * @return 结果集
	     */
	 List<SampleScrapped>  myselect(IRequest requestContext,SampleScrapped dto,int page, int pageSize); 
	 /**
	     * 测试样品页面查询
	     * @param dto 查询内容
	     * @param page 页码
	     * @param pageSize 页大小
	     * @param request 请求
	     * @return 结果集
	     */
	 List<SampleScrapped>  myselectforcs(IRequest requestContext,SampleScrapped dto,int page, int pageSize); 
	 /**
	     * 测试样品新建
	     * @param dto 操作数据集
	     * @param result 结果参数
	     * @param request 请求
	     * @return 操作结果
	     */
	 ResponseData addNewforcs(SampleScrapped dto,IRequest requestCtx, HttpServletRequest request);
}