package com.hand.spc.ecr_main.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.ecr_main.dto.EcrSolutionMain;
import com.hand.spc.ecr_main.view.EcrSolutionMainV0;

public interface IEcrSolutionMainService extends IBaseService<EcrSolutionMain>, ProxySelf<IEcrSolutionMainService>{
	/*
	 *   改善方案数据查询
	 */
	public List<EcrSolutionMainV0> baseQuery(IRequest iRequest,EcrSolutionMainV0 dto) ;
}