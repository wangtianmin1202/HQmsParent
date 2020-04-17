package com.hand.npi.npi_route.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.npi.npi_route.dto.TechnologyWpAction;
import com.hand.npi.npi_route.dto.TechnologyWpStandardActionDetail;

public interface ITechnologyWpStandardActionDetailService extends IBaseService<TechnologyWpStandardActionDetail>, ProxySelf<ITechnologyWpStandardActionDetailService>{
	
	List<TechnologyWpStandardActionDetail> queryActionInfo(IRequest request, TechnologyWpAction condition);

}