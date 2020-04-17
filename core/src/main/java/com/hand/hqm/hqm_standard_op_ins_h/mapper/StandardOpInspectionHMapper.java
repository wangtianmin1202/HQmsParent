package com.hand.hqm.hqm_standard_op_ins_h.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_standard_op_ins_h.dto.StandardOpInspectionH;

public interface StandardOpInspectionHMapper extends Mapper<StandardOpInspectionH>{
	List<StandardOpInspectionH> myselect(StandardOpInspectionH dto);
}