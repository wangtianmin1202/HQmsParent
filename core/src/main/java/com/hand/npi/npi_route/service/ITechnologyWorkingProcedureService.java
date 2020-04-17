package com.hand.npi.npi_route.service;

import java.util.List;
import java.util.Map;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.npi.npi_route.dto.TechnologyWorkingProcedure;
import com.hand.npi.npi_technology.dto.EbomDetail;
import com.hand.npi.npi_technology.dto.EbomMain;

public interface ITechnologyWorkingProcedureService extends IBaseService<TechnologyWorkingProcedure>, ProxySelf<ITechnologyWorkingProcedureService>{
	ResponseData addData(IRequest request, TechnologyWorkingProcedure list);
	ResponseData updateData(IRequest request, TechnologyWorkingProcedure list);
	
	EbomMain queryEBomVersion (IRequest request, EbomMain dto);
	List<EbomDetail> qeuryEBomPart (IRequest request, EbomMain dto, int pageNum, int pageSize);

	List<TechnologyWorkingProcedure> selectWpInfo(IRequest request, TechnologyWorkingProcedure dto, int pageNum, int pageSize);

}