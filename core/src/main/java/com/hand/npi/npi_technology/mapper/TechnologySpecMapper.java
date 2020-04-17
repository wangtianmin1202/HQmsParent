package com.hand.npi.npi_technology.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.npi.npi_technology.dto.TechnologySpec;
import com.hand.wfl.util.DropDownListDto;

public interface TechnologySpecMapper extends Mapper<TechnologySpec>{
	
	List<TechnologySpec> selectMaxNumber(TechnologySpec dto);
	
	List<DropDownListDto> queryStandardActionName(@Param("standardActionName") String standardActionName);
	
	/**
	 * 测试动作下拉列表查询
	 * @param standardActionName
	 * @return
	 */
	List<DropDownListDto> queryTestActionName(@Param("standardActionName") String standardActionName);
	
	List<DropDownListDto> queryAuxiliaryActionName(@Param("auxiliaryActionName") String standardActionName);
	
	List<DropDownListDto> queryMaterielName(@Param("materielName") String materielName);
	
	List<DropDownListDto> queryCharacteristicName(@Param("characteristicName") String characteristicName);
	
	List<TechnologySpec> queryTechnologySpecList(TechnologySpec dto);
	List<TechnologySpec> querySpecData(TechnologySpec dto);
	List<TechnologySpec> querySpecInfo(TechnologySpec dto);
	List<TechnologySpec> queryHisData(TechnologySpec dto);

}