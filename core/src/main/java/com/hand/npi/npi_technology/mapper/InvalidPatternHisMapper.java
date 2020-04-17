package com.hand.npi.npi_technology.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.npi.npi_technology.dto.InvalidPatternHis;

public interface InvalidPatternHisMapper extends Mapper<InvalidPatternHis>{

	List<InvalidPatternHis> selectByLastUpdateDate(InvalidPatternHis dto);
}