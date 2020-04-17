package com.hand.npi.npi_technology.mapper;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.npi.npi_technology.dto.SopInfo;

public interface SopInfoMapper extends Mapper<SopInfo>{
	
	/**
	 * @Description: 获取序列的值 用作code的新建
	 * @return
	 */
	String getCodeSeq();
	String queryFileId( @Param("dto") SopInfo dto);

}