package com.hand.npi.npi_technology.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.npi.npi_technology.dto.SparePartMenuItem;
import com.hand.npi.npi_technology.dto.TechnologySparePart;
import com.hand.npi.npi_technology.dto.TechnologySpecDetail;

public interface ITechnologySpecDetailService extends IBaseService<TechnologySpecDetail>, ProxySelf<ITechnologySpecDetailService>{
	List<TechnologySpecDetail> queryData(IRequest requestContext, TechnologySpecDetail dto, int pageNum, int pageSize);

	public List<TechnologySpecDetail> queryBySkuCode(IRequest requestContext, TechnologySpecDetail dto, int pageNum, int pageSize);
}