package com.hand.npi.npi_technology.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.npi.npi_technology.dto.PreventionMeasure;

public interface PreventionMeasureMapper extends Mapper<PreventionMeasure>{
	
	List<PreventionMeasure> selectMaxNumber(PreventionMeasure dto);
	
	List<PreventionMeasure> queryPreventionMeasureList(PreventionMeasure dto);
	
	List<PreventionMeasure> queryByPatId(PreventionMeasure dto);

}