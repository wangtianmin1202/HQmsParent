package com.hand.hqm.hqm_sample_scrapped_after.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_sample_scrapped_after.dto.SampleScrappedAfter;

public interface ISampleScrappedAfterService extends IBaseService<SampleScrappedAfter>, ProxySelf<ISampleScrappedAfterService>{

	/**
	 * 添加单条
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @param requestCtx
	 * @param request
	 * @return
	 */
	ResponseData addOne( SampleScrappedAfter dto,IRequest requestCtx, HttpServletRequest request);

	
	/**
	 * 提交
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<SampleScrappedAfter> commit(IRequest requestContext, SampleScrappedAfter dto);
	
	
	/**
	 * 
	 * @description 审批
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestContext
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	List<SampleScrappedAfter> approval(IRequest requestContext, SampleScrappedAfter dto) throws Exception;
	
	
	/**
	 * 
	 * @description 拒绝
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestContext
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	List<SampleScrappedAfter> turnDown(IRequest requestContext, SampleScrappedAfter dto) throws Exception;
	
	/**
	 * 关闭
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestContext
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	List<SampleScrappedAfter> close(IRequest requestContext, SampleScrappedAfter dto) throws Exception;

	
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
	List<SampleScrappedAfter> reSelect(IRequest requestContext, SampleScrappedAfter dto, int page, int pageSize);

}