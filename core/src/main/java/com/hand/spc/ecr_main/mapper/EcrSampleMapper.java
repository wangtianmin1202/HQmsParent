package com.hand.spc.ecr_main.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.ecr_main.dto.EcrSample;
import com.hand.spc.ecr_main.view.EcrSampleV0;

public interface EcrSampleMapper extends Mapper<EcrSample>{
	public List<EcrSampleV0> baseQuery(EcrSample dto);
}