package com.hand.npi.npi_technology.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.npi.npi_technology.dto.InvalidCause;
import com.hand.npi.npi_technology.dto.SopRouteRef;

public interface ISopRouteRefService extends IBaseService<SopRouteRef>, ProxySelf<ISopRouteRefService>{

	List<SopRouteRef> queryData(IRequest request, SopRouteRef dto, int pageNum, int pageSize );
	List<SopRouteRef> hisQuery(IRequest request, SopRouteRef dto, int pageNum, int pageSize );
	ResponseData fileUpload(IRequest requestCtx,HttpServletRequest request);
	ResponseData sopChange(IRequest requestCtx,SopRouteRef dto);

}