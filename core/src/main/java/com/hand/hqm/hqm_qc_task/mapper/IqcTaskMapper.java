package com.hand.hqm.hqm_qc_task.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_qc_task.dto.IqcTask;

public interface IqcTaskMapper extends Mapper<IqcTask>{

	/**
	 * @description 基础查询
	 * @author tianmin.wang
	 * @date 2019年11月18日 
	 * @param dto
	 * @return
	 */
	List<IqcTask> reSelect(IqcTask dto);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月24日 
	 * @param sourceOrder
	 * @return
	 */
	List<IqcTask> getIqcTaskEarlistBySourceOrder(String sourceOrder);

}