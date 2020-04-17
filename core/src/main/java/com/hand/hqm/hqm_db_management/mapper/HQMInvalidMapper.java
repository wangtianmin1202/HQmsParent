package com.hand.hqm.hqm_db_management.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_db_management.dto.HQMInvalid;
import com.hand.hqm.hqm_db_management.dto.HQMInvalidTree;

public interface HQMInvalidMapper extends Mapper<HQMInvalid>{
	public List<HQMInvalid> query();
	List<HQMInvalid> selectInvalidByParent(HQMInvalid dto);
}