package com.hand.hqm.hqm_inspection_attribute.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_inspection_attribute.dto.InspectionAttribute;

public interface InspectionAttributeMapper extends Mapper<InspectionAttribute>{

	List<InspectionAttribute> reSelect(InspectionAttribute dto);

}