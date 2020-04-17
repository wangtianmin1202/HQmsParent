package com.hand.npi.npi_route.mapper;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.mybatis.common.Mapper;
import com.hand.npi.npi_route.dto.TechnologyWpAction;
import com.hand.npi.npi_route.dto.TechnologyWpStandardActionDetail;

public interface TechnologyWpStandardActionDetailMapper extends Mapper<TechnologyWpStandardActionDetail>{
	List<TechnologyWpStandardActionDetail> queryActionInfo(TechnologyWpAction condition);
}