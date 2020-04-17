package com.hand.npi.npi_technology.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.npi.npi_technology.dto.TechnologySparePart;

public interface TechnologySparePartMapper extends Mapper<TechnologySparePart>{

	/**
	 * @Description:查询根节点
	 * @param dto
	 * @return
	 */
	List<TechnologySparePart> queryRootData(TechnologySparePart dto);

	/**
	 * @Description:
	 * @param sparePart
	 * @return
	 */
	List<TechnologySparePart> queryLeafData(TechnologySparePart sparePart);

	/**
	 * @Description:
	 * @param sparePartOne
	 * @return
	 */
	List<TechnologySparePart> queryThreeLevelByOneLevel(TechnologySparePart sparePartOne);
	
	/**
	 * @Description:
	 * @param sparePartOne
	 * @return
	 */
	List<TechnologySparePart> querySparePartLov(TechnologySparePart sparePartOne);
	
	Long hasChild(@Param("parentId") String parentId);

}