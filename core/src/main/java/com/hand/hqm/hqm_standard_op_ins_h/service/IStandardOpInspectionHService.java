package com.hand.hqm.hqm_standard_op_ins_h.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_sam_use_order_h.dto.SamUseOrderH;
import com.hand.hqm.hqm_standard_op_ins_h.dto.StandardOpInspectionH;

public interface IStandardOpInspectionHService extends IBaseService<StandardOpInspectionH>, ProxySelf<IStandardOpInspectionHService>{
	List<StandardOpInspectionH>  myselect(IRequest requestContext,StandardOpInspectionH dto,int page, int pageSize);

	int reBatchDelete(List<StandardOpInspectionH> dto);

	List<StandardOpInspectionH> save(IRequest requestContext, StandardOpInspectionH dto); 
	
}