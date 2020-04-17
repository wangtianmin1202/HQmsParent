package com.hand.hqm.hqm_sop_pqc_control_h.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_sop_pqc_control_h.dto.SopPqcControlH;
import com.hand.hqm.hqm_supp_item_exemption.dto.SuppItemExemption;

public interface SopPqcControlHMapper extends Mapper<SopPqcControlH>{
	 List<SopPqcControlH> myselect(SopPqcControlH dto);

}