package com.hand.npi.npi_technology.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.npi.npi_technology.dto.SopRouteRef;
import com.hand.npi.npi_technology.dto.TechnologySparePartDetails;

public interface SopRouteRefMapper extends Mapper<SopRouteRef>{
	/**
	 * @Description:
	 * @param dto
	 * @return
	 */
	List<SopRouteRef> dataQuery( @Param("dto")SopRouteRef dto);
	List<SopRouteRef> hisQuery( @Param("dto")SopRouteRef dto);
	

}