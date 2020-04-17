package com.hand.hqm.hqm_use_detail.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_sample_use.dto.SampleUse;
import com.hand.hqm.hqm_scrapped_detail.dto.ScrappedDetail;
import com.hand.hqm.hqm_use_detail.dto.UseDetail;

public interface IUseDetailService extends IBaseService<UseDetail>, ProxySelf<IUseDetailService>{
	/**
     * 新建行表
     * @param dto 查询内容
     * @param request 请求
     * @return 结果集
     */
	ResponseData addline(UseDetail dto,IRequest requestCtx, HttpServletRequest request);
	/**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
	List<UseDetail>  myselect(IRequest requestContext,UseDetail dto,int page, int pageSize); 
	/**
     * 样品查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
	List<UseDetail>  selectforsample(IRequest requestContext,UseDetail dto,int page, int pageSize); 
	/**
     * 删除
     * @param dto 查询内容
     * @param request 请求
     * @return  
     */
	void deleteRow(UseDetail dto);
}