package com.hand.hqm.hqm_use_detail_after.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_use_detail_after.dto.UseDetailAfter;

public interface IUseDetailAfterService extends IBaseService<UseDetailAfter>, ProxySelf<IUseDetailAfterService>{

	List<UseDetailAfter> reSelect(IRequest requestContext, UseDetailAfter dto, int page, int pageSize);

	UseDetailAfter addOne(IRequest requestCtx, UseDetailAfter dto);

}