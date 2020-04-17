package com.hand.npi.npi_technology.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.npi.npi_technology.dto.TechnologySpecDetail;
import com.hand.npi.npi_technology.dto.TechnologySpecMatDetail;

public interface TechnologySpecMatDetailMapper extends Mapper<TechnologySpecMatDetail>{

	List<TechnologySpecMatDetail> dataQuery( @Param("dto")TechnologySpecMatDetail dto);

	
}