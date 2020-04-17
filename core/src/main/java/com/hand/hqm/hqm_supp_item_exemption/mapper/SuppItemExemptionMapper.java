package com.hand.hqm.hqm_supp_item_exemption.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_asl_iqc_control.dto.AslIqcControl;
import com.hand.hqm.hqm_supp_item_exemption.dto.SuppItemExemption;

public interface SuppItemExemptionMapper extends Mapper<SuppItemExemption>{
	
	 List<SuppItemExemption> myselect(SuppItemExemption dto);
	

}