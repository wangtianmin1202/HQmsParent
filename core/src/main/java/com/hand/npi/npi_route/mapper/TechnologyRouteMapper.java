package com.hand.npi.npi_route.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.npi.npi_route.dto.TechnologyRoute;
import com.hand.npi.npi_technology.dto.EbomDetail;
import com.hand.npi.npi_technology.dto.QuickTechRouteDto;
import com.hand.wfl.util.DropDownListDto;

public interface TechnologyRouteMapper extends Mapper<TechnologyRoute>{
	List<DropDownListDto> queryOldSku(@Param("dto") QuickTechRouteDto dto);
	List<DropDownListDto> queryRouteVersion(@Param("dto") QuickTechRouteDto dto);
	List<DropDownListDto> queryNewSku(@Param("dto") QuickTechRouteDto dto);
	List<EbomDetail> queryOldEbom(@Param("dto") QuickTechRouteDto dto);
	List<EbomDetail> queryNewEbom(@Param("dto") QuickTechRouteDto dto);
	
	List<EbomDetail> getOldMinusNew(@Param("dto") QuickTechRouteDto dto);
	List<EbomDetail> getNewMinOld(@Param("dto") QuickTechRouteDto dto);
	String getNewEbomVersion(Float sku);
}