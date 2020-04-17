package com.hand.npi.npi_technology.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.npi.npi_technology.dto.TechnologySpecDetail;
import com.hand.npi.npi_technology.dto.TechnologySpecMatDetail;

public interface ITechnologySpecMatDetailService extends IBaseService<TechnologySpecMatDetail>, ProxySelf<ITechnologySpecMatDetailService>{
	List<TechnologySpecMatDetail> queryData(IRequest requestContext, TechnologySpecMatDetail dto, int pageNum, int pageSize);
}