package com.hand.npi.npi_route.service;

import java.util.List;
import java.util.Map;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.npi.npi_route.dto.TechnologyRoute;
import com.hand.npi.npi_route.dto.TechnologyWorkingProcedure;
import com.hand.npi.npi_technology.dto.EbomDetail;
import com.hand.npi.npi_technology.dto.QuickTechRouteDto;
import com.hand.wfl.util.DropDownListDto;

public interface ITechnologyRouteService extends IBaseService<TechnologyRoute>, ProxySelf<ITechnologyRouteService>{
	List<TechnologyRoute> addData(IRequest request, List<TechnologyRoute> list);
	List<DropDownListDto> queryOldSku(IRequest request, QuickTechRouteDto dto);
	List<DropDownListDto> queryRouteVersion(IRequest request, QuickTechRouteDto dto);
	List<DropDownListDto> queryNewSku(IRequest request, QuickTechRouteDto dto);
	List<EbomDetail> queryOldEbom(IRequest request, QuickTechRouteDto dto);
	List<EbomDetail> queryNewEbom(IRequest request, QuickTechRouteDto dto);
	Map<String, Object> checkData(IRequest request, QuickTechRouteDto dto);
	ResponseData copyData(IRequest request, QuickTechRouteDto dto);
}