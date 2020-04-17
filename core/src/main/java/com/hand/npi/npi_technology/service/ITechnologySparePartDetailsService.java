package com.hand.npi.npi_technology.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.npi.npi_technology.dto.TechnologySparePartDetails;

public interface ITechnologySparePartDetailsService extends IBaseService<TechnologySparePartDetails>, ProxySelf<ITechnologySparePartDetailsService>{

	/**
	 * @Description:零件主查询
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<TechnologySparePartDetails> queryByCondition(IRequest requestContext, TechnologySparePartDetails dto, int page, int pageSize);

	/**
	 * @Description:添加和编辑零件信息
	 * @param requestCtx
	 * @param dto
	 * @return
	 */
	List<TechnologySparePartDetails> add(IRequest requestCtx, TechnologySparePartDetails dto);

}