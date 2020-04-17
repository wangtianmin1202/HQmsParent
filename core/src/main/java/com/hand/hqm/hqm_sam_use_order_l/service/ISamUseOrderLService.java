package com.hand.hqm.hqm_sam_use_order_l.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_sam_use_order_l.dto.SamUseOrderL;

public interface ISamUseOrderLService extends IBaseService<SamUseOrderL>, ProxySelf<ISamUseOrderLService>{
	/**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
	 List<SamUseOrderL>  myselect(IRequest requestContext,SamUseOrderL dto,int page, int pageSize); 
	 /**
	     * 保存头行表数据
	     * @param dto 
	     * @param request 请求
	     * @return 结果集
	     */
	 ResponseData saveHeadLine(IRequest requestContext, List<SamUseOrderL> dto);
	 /**
	     * 保存行表数据
	     * @param dto 
	     * @param request 请求
	     * @return 结果集
	     */
	 ResponseData dealSaveLine(IRequest requestContext, List<SamUseOrderL> dto);
	 /**
	     * 保存行表数据
	     * @param dto 
	     * @param request 请求
	     * @return 结果集
	     */
	 List<SamUseOrderL> saveLine(IRequest requestContext, List<SamUseOrderL> dto);
}