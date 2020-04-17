package com.hand.hqm.hqm_db_management.mapper;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_db_management.dto.HQMStructure;

public interface HQMStructureMapper extends Mapper<HQMStructure>{
	public HQMStructure structureNamecount(@Param("structureName") String structureName);
}