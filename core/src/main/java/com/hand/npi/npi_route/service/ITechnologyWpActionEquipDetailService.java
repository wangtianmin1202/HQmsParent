package com.hand.npi.npi_route.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.npi.npi_route.dto.TechnologyWpActionEquipDetail;

public interface ITechnologyWpActionEquipDetailService extends IBaseService<TechnologyWpActionEquipDetail>, ProxySelf<ITechnologyWpActionEquipDetailService>{
	List<TechnologyWpActionEquipDetail> addNewData(IRequest request, TechnologyWpActionEquipDetail list);
}