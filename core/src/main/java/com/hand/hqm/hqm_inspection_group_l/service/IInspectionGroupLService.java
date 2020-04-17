package com.hand.hqm.hqm_inspection_group_l.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_inspection_group_l.dto.InspectionGroupL;
import com.hand.hqm.hqm_iqc_inspection_template_l.dto.IqcInspectionTemplateL;

public interface IInspectionGroupLService extends IBaseService<InspectionGroupL>, ProxySelf<IInspectionGroupLService>{
	 ResponseData saveHead(IRequest requestContext, InspectionGroupL dto);
	 ResponseData updateLine(IRequest requestContext, List<InspectionGroupL> dto);
	 List<InspectionGroupL>  myselect(IRequest requestContext,InspectionGroupL dto,int page, int pageSize); 
/*	 ResponseData saveLine(IRequest requestContext, List<InspectionGroupL> dto);
*/	 List<InspectionGroupL>  selecthead(IRequest requestContext,InspectionGroupL dto,int page, int pageSize); 
	 List<InspectionGroupL>  selectTb(IRequest requestContext,InspectionGroupL dto,int page, int pageSize); 
	 List<InspectionGroupL>  selectheadincopy(IRequest requestContext,InspectionGroupL dto,int page, int pageSize); 
      List<InspectionGroupL> historynumberUpdate(IRequest request, List<InspectionGroupL> list) throws Exception;

}