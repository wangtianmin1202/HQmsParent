package com.hand.npi.npi_technology.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.npi.npi_route.dto.TechnologyWpAction;
import com.hand.npi.npi_technology.dto.ComposeProduct;

public interface IComposeProductService extends IBaseService<ComposeProduct>, ProxySelf<IComposeProductService>{
	
	List<ComposeProduct> addData(IRequest request, List<ComposeProduct> dtos);

}