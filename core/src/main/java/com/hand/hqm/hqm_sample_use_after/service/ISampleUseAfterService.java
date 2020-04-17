package com.hand.hqm.hqm_sample_use_after.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_sample_use_after.dto.SampleUseAfter;

public interface ISampleUseAfterService extends IBaseService<SampleUseAfter>, ProxySelf<ISampleUseAfterService>{

	List<SampleUseAfter> addOne(IRequest requestContext, SampleUseAfter dto, int page, int pageSize);

	List<SampleUseAfter> reSelect(IRequest requestContext, SampleUseAfter dto, int page, int pageSize);

	List<SampleUseAfter> commit(IRequest requestContext, SampleUseAfter dto);

	
	/**
	 * 审批
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestContext
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	List<SampleUseAfter> approval(IRequest requestContext, SampleUseAfter dto) throws Exception;

	
	/**
	 * 拒绝
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestContext
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	List<SampleUseAfter> turnDown(IRequest requestContext, SampleUseAfter dto) throws Exception;

	
	/**
	 * 执行
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestContext
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	List<SampleUseAfter> execute(IRequest requestContext, SampleUseAfter dto) throws Exception;

	
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
	List<SampleUseAfter> close(IRequest requestContext, SampleUseAfter dto) throws Exception;

}