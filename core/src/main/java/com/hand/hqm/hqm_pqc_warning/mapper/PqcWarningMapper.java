package com.hand.hqm.hqm_pqc_warning.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_pqc_warning.dto.PqcWarning;

public interface PqcWarningMapper extends Mapper<PqcWarning>{

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月26日 
	 * @param dto
	 * @return
	 */
	List<PqcWarning> reSelect(PqcWarning dto);

}