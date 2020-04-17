package com.hand.hqm.hqm_sample_use.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_sample_account.dto.SampleAccount;
import com.hand.hqm.hqm_sample_scrapped.dto.SampleScrapped;
import com.hand.hqm.hqm_sample_use.dto.SampleUse;

public interface ISampleUseService extends IBaseService<SampleUse>, ProxySelf<ISampleUseService>{
	 /**
     * 查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
	 List<SampleUse>  myselect(IRequest requestContext,SampleUse dto,int page, int pageSize); 
	 /**
	     * 提交
	     * @description
	     * @author tianmin.wang
	     * @date 2019年11月22日 
	     * @param request
	     * @param dto
	     * @return
	     */
	 ResponseData comfirm(IRequest requestContext, List<String> headList);
	 /**
	     * 新建测试样品
	     * @param dto 操作数据集
	     * @param result 结果参数
	     * @param request 请求
	     * @return 操作结果
	     */
	 ResponseData addNewforcs(SampleUse dto,IRequest requestCtx, HttpServletRequest request);
	 /**
	     * 查询历史
	     * @param dto 查询内容
	     * @param page 页码
	     * @param pageSize 页大小
	     * @param request 请求
	     * @return 结果集
	     */
	 List<SampleUse>  select_his(IRequest requestContext,SampleUse dto,int page, int pageSize);
	/**
	 * @Description:领用
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	ResponseData forUse(IRequest requestContext, List<SampleAccount> dto);
	/**
	 * @Description:报废
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	ResponseData scrap(IRequest requestContext, List<SampleAccount> dto); 
	

}