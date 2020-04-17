package com.hand.hqm.hqm_temporary_inspection.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_temporary_inspection.dto.TemporaryInspection;

public interface TemporaryInspectionMapper extends Mapper<TemporaryInspection>{

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月10日 
	 * @param dto
	 * @return
	 */
	List<TemporaryInspection> reSelect(TemporaryInspection dto);

}