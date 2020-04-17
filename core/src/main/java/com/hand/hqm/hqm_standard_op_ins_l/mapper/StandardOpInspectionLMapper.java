package com.hand.hqm.hqm_standard_op_ins_l.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_standard_op_ins_l.dto.StandardOpInspectionL;

public interface StandardOpInspectionLMapper extends Mapper<StandardOpInspectionL>{
	List<StandardOpInspectionL> myselect(StandardOpInspectionL dto);
}