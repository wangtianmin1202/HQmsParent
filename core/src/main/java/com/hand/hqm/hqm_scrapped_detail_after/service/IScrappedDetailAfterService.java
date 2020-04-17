package com.hand.hqm.hqm_scrapped_detail_after.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_scrapped_detail_after.dto.ScrappedDetailAfter;

public interface IScrappedDetailAfterService extends IBaseService<ScrappedDetailAfter>, ProxySelf<IScrappedDetailAfterService>{

	
	/**
	 * 新建保存
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestCtx
	 * @param dto
	 * @return
	 */
	ScrappedDetailAfter addOne(IRequest requestCtx, ScrappedDetailAfter dto);

	List<ScrappedDetailAfter> reSelect(IRequest requestContext, ScrappedDetailAfter dto, int page, int pageSize);

}