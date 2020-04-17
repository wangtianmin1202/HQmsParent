package com.hand.npi.npi_technology.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.npi.npi_technology.dto.TechnologyStandardAction;

public interface TechnologyStandardActionMapper extends Mapper<TechnologyStandardAction>{
	
	List<TechnologyStandardAction> selectMaxNumber(TechnologyStandardAction dto);

}