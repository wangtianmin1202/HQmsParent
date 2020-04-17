package com.hand.hqm.hqm_sample.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_sample.dto.Sample;

public interface ISampleService extends IBaseService<Sample>, ProxySelf<ISampleService>{
	
	
	/**
	 * 添加
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @param requestCtx
	 * @param request
	 * @return
	 */
    ResponseData addNew(Sample dto,IRequest requestCtx, HttpServletRequest request);
    
    
    List<Sample>  selectforuse(IRequest requestContext,Sample dto,int page, int pageSize); 
    
    /**
     * 基础查询
     * @description
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param requestContext
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    List<Sample>  myselect(IRequest requestContext,Sample dto,int page, int pageSize); 
    
    /**
     * 提交
     * @description
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param requestContext
     * @param headList
     * @return
     */
    List<Sample> confirm(IRequest requestContext, List<Float> headList);
    
    /**
     * 确认
     * @description
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param requestContext
     * @param headList
     * @return
     */
    List<Sample> deal(IRequest requestContext, List<String> headList);
    
    
    List<Sample> useDeal(IRequest requestContext, List<Sample> headList);
}