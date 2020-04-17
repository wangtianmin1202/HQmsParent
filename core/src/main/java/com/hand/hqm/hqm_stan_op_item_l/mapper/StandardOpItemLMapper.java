package com.hand.hqm.hqm_stan_op_item_l.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_stan_op_item_l.dto.StandardOpItemL;
import com.hand.hqm.hqm_standard_op_ins_l.dto.StandardOpInspectionL;

public interface StandardOpItemLMapper extends Mapper<StandardOpItemL>{
	List<StandardOpItemL> myselect(StandardOpItemL dto);
}