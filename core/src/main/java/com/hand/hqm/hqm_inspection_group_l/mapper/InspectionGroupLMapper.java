package com.hand.hqm.hqm_inspection_group_l.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_dfmea_detail.dto.DfmeaDetail;
import com.hand.hqm.hqm_inspection_group_h.dto.InspectionGroupH;
import com.hand.hqm.hqm_inspection_group_l.dto.InspectionGroupL;
import com.hand.hqm.hqm_iqc_inspection_template_l.dto.IqcInspectionTemplateL;

public interface InspectionGroupLMapper extends Mapper<InspectionGroupL>{
	List<InspectionGroupL> myselect(InspectionGroupL dto);
	List<InspectionGroupL> selecthead(InspectionGroupL dto);
	int selectcount(InspectionGroupL record);
	public List<InspectionGroupL> selectTb(InspectionGroupL dto);
	public List<InspectionGroupL> selectheadincopy(InspectionGroupL dto);

}