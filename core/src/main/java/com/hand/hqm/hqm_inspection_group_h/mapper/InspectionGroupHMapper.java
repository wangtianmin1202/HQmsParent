package com.hand.hqm.hqm_inspection_group_h.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_fmea.dto.Fmea;
import com.hand.hqm.hqm_inspection_group_h.dto.InspectionGroupH;

public interface InspectionGroupHMapper extends Mapper<InspectionGroupH>{
	List<InspectionGroupH> myselect(InspectionGroupH dto);
	
	List<InspectionGroupH> itemselect(InspectionGroupH dto);
}