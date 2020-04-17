package com.hand.spc.ecr_main.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.ecr_main.dto.EcrSolutionSkuRelation;
import com.hand.spc.ecr_main.view.EcrSolutionSkuV3;

public interface EcrSolutionSkuRelationMapper extends Mapper<EcrSolutionSkuRelation>{

	public List<EcrSolutionSkuRelation> getSpItem(EcrSolutionSkuV3 dto);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
}