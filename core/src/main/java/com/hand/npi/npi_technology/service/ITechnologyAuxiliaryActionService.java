package com.hand.npi.npi_technology.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.npi.npi_technology.dto.TechnologyAuxiliaryAction;

public interface ITechnologyAuxiliaryActionService extends IBaseService<TechnologyAuxiliaryAction>, ProxySelf<ITechnologyAuxiliaryActionService>{

	ResponseData addNewTechnologyAuxiliaryAction(List<TechnologyAuxiliaryAction> dto, IRequest requestCtx, HttpServletRequest request);
}