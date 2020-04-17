package com.hand.hqm.hqm_qc_task.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_qc_task.dto.OqcTask;

public interface OqcTaskMapper extends Mapper<OqcTask> {
	List<OqcTask> reSelect(OqcTask dto);
}