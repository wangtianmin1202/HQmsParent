package com.hand.hqm.hqm_sop_pqc_control_h.service;

import java.util.List;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_sop_pqc_control_h.dto.SopPqcControlH;


public interface ISopPqcControlHService extends IBaseService<SopPqcControlH>, ProxySelf<ISopPqcControlHService>{
	
	List<SopPqcControlH> myselect(IRequest requestContext,SopPqcControlH dto,int page, int pageSize);

}