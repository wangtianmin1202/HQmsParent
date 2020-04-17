package com.hand.npi.npi_route.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.npi.npi_route.dto.TechnologyWpActionEquipDetail;
import com.hand.npi.npi_route.dto.TechnologyWpStandardActionDetail;

public interface TechnologyWpActionEquipDetailMapper extends Mapper<TechnologyWpActionEquipDetail>{
	int updateData(TechnologyWpActionEquipDetail record);
	List<TechnologyWpActionEquipDetail> selectInfoAux(TechnologyWpActionEquipDetail equipDetail);
	List<TechnologyWpActionEquipDetail> selectInfoSta(TechnologyWpActionEquipDetail equipDetail);
}