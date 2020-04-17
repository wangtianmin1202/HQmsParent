package com.hand.hqm.hqm_standard_op_ins_l.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_sam_use_order_l.dto.SamUseOrderL;
import com.hand.hqm.hqm_standard_op_ins_l.dto.StandardOpInspectionL;

public interface IStandardOpInspectionLService extends IBaseService<StandardOpInspectionL>, ProxySelf<IStandardOpInspectionLService>{

	 List<StandardOpInspectionL>  myselect(IRequest requestContext,StandardOpInspectionL dto,int page, int pageSize); 
	 List<StandardOpInspectionL> saveHeadLine(IRequest requestContext, List<StandardOpInspectionL> dto) throws Exception;
}