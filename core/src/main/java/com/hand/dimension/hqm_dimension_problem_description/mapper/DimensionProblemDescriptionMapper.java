package com.hand.dimension.hqm_dimension_problem_description.mapper;

import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

import com.hand.dimension.hqm_dimension_problem_description.dto.DimensionProblemDescription;

public interface DimensionProblemDescriptionMapper extends Mapper<DimensionProblemDescription>{
	/**
	 * 
	 * @description 查询
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param dto
	 * @return
	 */
	List<DimensionProblemDescription> reSelect(DimensionProblemDescription dto);

	/**
	 * 
	 * @description 相同物料的所有数据
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param dto
	 * @return
	 */
	List<DimensionProblemDescription> sameItemDimension(DimensionProblemDescription dto);
	
	/**
	 * 
	 * @description 相同nggroup组的所有数据
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param dto
	 * @return
	 */
	List<DimensionProblemDescription> sameItemDimensionNgCodeGroup(DimensionProblemDescription dto);
}