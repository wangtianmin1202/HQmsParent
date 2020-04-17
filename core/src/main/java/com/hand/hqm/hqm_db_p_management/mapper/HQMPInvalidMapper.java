package com.hand.hqm.hqm_db_p_management.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_db_p_management.dto.HQMPInvalid;

public interface HQMPInvalidMapper extends Mapper<HQMPInvalid>{
	public List<HQMPInvalid> query();

}