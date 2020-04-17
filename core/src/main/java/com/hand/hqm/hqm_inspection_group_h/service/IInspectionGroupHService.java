package com.hand.hqm.hqm_inspection_group_h.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_inspection_group_h.dto.InspectionGroupH;

public interface IInspectionGroupHService extends IBaseService<InspectionGroupH>, ProxySelf<IInspectionGroupHService>{
	 List<InspectionGroupH>  myselect(IRequest requestContext,InspectionGroupH dto,int page, int pageSize); 
	 
	 List<InspectionGroupH>  itemselect(IRequest requestContext,InspectionGroupH dto,int page, int pageSize); 

	 ResponseData distribute(InspectionGroupH dto,IRequest requestCtx, HttpServletRequest request);
	 


}