package com.hand.hqm.hqm_qc_task.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_qc_task.dto.PqcTask;

public interface PqcTaskMapper extends Mapper<PqcTask>{

	/**
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月27日 
	 * @return
	 */
	List<PqcTask> jobSelect();

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月27日 
	 * @param dto
	 * @return
	 */
	List<PqcTask> reSelect(PqcTask dto);
	
}