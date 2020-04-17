package com.hand.npi.npi_technology.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.npi.npi_technology.dto.PreventionMeasure;

public interface IPreventionMeasureService extends IBaseService<PreventionMeasure>, ProxySelf<IPreventionMeasureService>{
	
	ResponseData addNewPreventionMeasure(PreventionMeasure dto, IRequest requestCtx, HttpServletRequest request);
	String getPreventionMeasureNumber(String preventionMeasureNumber);
	
	List<PreventionMeasure> queryPreventionMeasureList(PreventionMeasure dto);
	List<PreventionMeasure> queryByPatId(IRequest request, PreventionMeasure condition, int pageNum, int pageSize);

}