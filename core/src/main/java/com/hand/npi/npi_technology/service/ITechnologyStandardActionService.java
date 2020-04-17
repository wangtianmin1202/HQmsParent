package com.hand.npi.npi_technology.service;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.npi.npi_technology.dto.TechnologyStandardAction;

public interface ITechnologyStandardActionService extends IBaseService<TechnologyStandardAction>, ProxySelf<ITechnologyStandardActionService>{

	ResponseData addNewTechnologyStandardAction(TechnologyStandardAction dto, IRequest requestCtx, HttpServletRequest request);
}