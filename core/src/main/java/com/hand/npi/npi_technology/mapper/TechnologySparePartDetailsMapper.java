package com.hand.npi.npi_technology.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.npi.npi_technology.dto.TechnologySparePartDetails;

public interface TechnologySparePartDetailsMapper extends Mapper<TechnologySparePartDetails>{

	/**
	 * @Description:
	 * @param dto
	 * @return
	 */
	List<TechnologySparePartDetails> queryByCondition(@Param("list")List<Float> list, @Param("dto")TechnologySparePartDetails dto);
	
	/**
	 * @Description: 获取序列的值 用作code的新建
	 * @return
	 */
	String getCodeSeq();
	
	Long hasChild(@Param("parentId") String parentId);
}