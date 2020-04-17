package com.hand.spc.ecr_main.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.ecr_main.dto.EcrSolutionMain;
import com.hand.spc.ecr_main.view.EcrSolutionMainV0;

public interface EcrSolutionMainMapper extends Mapper<EcrSolutionMain>{
	public List<EcrSolutionMainV0> baseQuery(EcrSolutionMainV0 dto);
}