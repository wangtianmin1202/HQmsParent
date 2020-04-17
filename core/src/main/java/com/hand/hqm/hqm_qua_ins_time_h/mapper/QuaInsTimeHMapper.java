package com.hand.hqm.hqm_qua_ins_time_h.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_qua_ins_time_h.dto.QuaInsTimeH;
import com.hand.hqm.hqm_standard_op_ins_h.dto.StandardOpInspectionH;

public interface QuaInsTimeHMapper extends Mapper<QuaInsTimeH>{
	List<QuaInsTimeH> myselect(QuaInsTimeH dto);
	
	List<QuaInsTimeH> jobSelect();
}