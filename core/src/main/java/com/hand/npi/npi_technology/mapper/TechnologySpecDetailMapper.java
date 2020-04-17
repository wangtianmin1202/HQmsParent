package com.hand.npi.npi_technology.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.npi.npi_technology.dto.SopRouteRef;
import com.hand.npi.npi_technology.dto.TechnologySpecDetail;

public interface TechnologySpecDetailMapper extends Mapper<TechnologySpecDetail>{
	String getCodeSeq();
	List<TechnologySpecDetail> dataQuery( @Param("dto")TechnologySpecDetail dto);

	List<TechnologySpecDetail> selectSpecDetailBySkuCode( @Param("dto")TechnologySpecDetail dto);
}