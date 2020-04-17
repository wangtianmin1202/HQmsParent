package com.hand.hqm.hqm_qc_task.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_qc_task.dto.FqcTask;

public interface FqcTaskMapper extends Mapper<FqcTask>{

	/**
	 * @description 基础查询
	 * @author tianmin.wang
	 * @date 2019年11月19日 
	 * @param dto
	 * @return
	 */
	List<FqcTask> reSelect(FqcTask dto);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月3日 
	 * @param sourceOrder
	 * @return
	 */
	List<FqcTask> getFqcTaskEarlistBySourceOrder(String sourceOrder);

}