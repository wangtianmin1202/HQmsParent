package com.hand.hqm.hqm_db_management.mapper;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_db_management.dto.HQMFunction;

public interface HQMFunctionMapper extends Mapper<HQMFunction>{
	public HQMFunction functionNamecount(@Param("functionName") String functionName);
}