package com.hand.spc.ecr_main.service;

import java.util.List;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.ecr_main.dto.EcrSolutionSkuRelation;
import com.hand.spc.ecr_main.view.EcrSolutionSkuV3;

public interface IEcrSolutionSkuRelationService extends IBaseService<EcrSolutionSkuRelation>, ProxySelf<IEcrSolutionSkuRelationService>{
	public List<EcrSolutionSkuRelation> getSpItem(EcrSolutionSkuV3 dto);   
}