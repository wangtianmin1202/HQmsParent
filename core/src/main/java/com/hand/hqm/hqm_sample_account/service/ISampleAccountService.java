package com.hand.hqm.hqm_sample_account.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_sample_account.dto.SampleAccount;
import com.hand.hqm.hqm_sample_account_after.dto.SampleAccountAfter;

public interface ISampleAccountService extends IBaseService<SampleAccount>, ProxySelf<ISampleAccountService>{
	/**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
	 List<SampleAccount>  myselect(IRequest requestContext,SampleAccount dto,int page, int pageSize); 

	    /**
	     * 新建
	     * @param dto 查询内容
	     * @param request 请求
	     * @return 结果集
	     */
	 ResponseData addNew(SampleAccount dto,IRequest requestCtx, HttpServletRequest request);
	 /**
	     * 测试样品页面查询
	     * @param dto 查询内容
	     * @param page 页码
	     * @param pageSize 页大小
	     * @param request 请求
	     * @return 结果集
	     */
	 List<SampleAccount>  myselectforcs(IRequest requestContext,SampleAccount dto,int page, int pageSize); 
	 /**
	    * 测试样品新建
	    * @param dto 查询内容
	    * @param request 请求
	    * @return 结果集
	    */
	 ResponseData addNewforcs(SampleAccount dto,IRequest requestCtx, HttpServletRequest request);
	 /**
	    * 购买样品新建
	    * @param dto 查询内容
	    * @param request 请求
	    * @return 结果集
	    */
	 ResponseData addnewforgm(SampleAccount dto,IRequest requestCtx, HttpServletRequest request);
	 /**
	    * 删除
	    * @param dto 查询内容
	    * @return 结果集
	    */
	 void deleteRow(SampleAccount dto);

}