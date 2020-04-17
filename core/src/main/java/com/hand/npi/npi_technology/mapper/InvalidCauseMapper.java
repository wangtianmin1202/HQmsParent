package com.hand.npi.npi_technology.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.npi.npi_technology.dto.InvalidCause;

public interface InvalidCauseMapper extends Mapper<InvalidCause>{
	
	List<InvalidCause> selectMaxNumber(InvalidCause dto);
	
	List<InvalidCause> selectInvalidCauseChild(InvalidCause dto);
	
	List<InvalidCause> queryInvalidCauseList(InvalidCause dto);

}