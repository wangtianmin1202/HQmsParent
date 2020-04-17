package com.hand.spc.ecr_main.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.spc.ecr_main.dto.EcrSolutionSkuRelation;
import com.hand.spc.ecr_main.mapper.EcrSolutionSkuRelationMapper;
import com.hand.spc.ecr_main.service.IEcrSolutionSkuRelationService;
import com.hand.spc.ecr_main.view.EcrSolutionSkuV3;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class EcrSolutionSkuRelationServiceImpl extends BaseServiceImpl<EcrSolutionSkuRelation> implements IEcrSolutionSkuRelationService{
	@Autowired
	private EcrSolutionSkuRelationMapper ecrSolutionSkuRelationMapper;
	
	public List<EcrSolutionSkuRelation> getSpItem(EcrSolutionSkuV3 dto){		
		return ecrSolutionSkuRelationMapper.getSpItem(dto);
	}
}