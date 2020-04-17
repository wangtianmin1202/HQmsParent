package com.hand.npi.npi_technology.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.npi.npi_technology.dto.InvalidPattern;
import com.hand.npi.npi_technology.dto.TechnologySpec;

public interface InvalidPatternMapper extends Mapper<InvalidPattern>{

	List<InvalidPattern> selectMaxNumber(InvalidPattern dto);
	
	
	List<InvalidPattern> selectPatternChild(InvalidPattern dto);
	
	List<InvalidPattern> queryNewVersionData(InvalidPattern dto);
	
	List<InvalidPattern> queryHisData(InvalidPattern dto);

}