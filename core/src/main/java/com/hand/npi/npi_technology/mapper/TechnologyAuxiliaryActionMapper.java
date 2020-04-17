package com.hand.npi.npi_technology.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.npi.npi_technology.dto.TechnologyAuxiliaryAction;

public interface TechnologyAuxiliaryActionMapper extends Mapper<TechnologyAuxiliaryAction>{
	
	List<TechnologyAuxiliaryAction> selectMaxNumber(TechnologyAuxiliaryAction dto);

}