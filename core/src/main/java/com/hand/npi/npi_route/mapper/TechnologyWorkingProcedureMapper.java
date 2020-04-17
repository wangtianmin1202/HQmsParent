package com.hand.npi.npi_route.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.npi.npi_route.dto.TechnologyWorkingProcedure;

import java.util.List;

public interface TechnologyWorkingProcedureMapper extends Mapper<TechnologyWorkingProcedure>{
	//Long checkWpNameChange(TechnologyWorkingProcedure dto);
	Float getMaxSerialNumber(TechnologyWorkingProcedure dto);

	List<TechnologyWorkingProcedure> queryWpInfo(TechnologyWorkingProcedure dto);
}
