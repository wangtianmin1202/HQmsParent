package com.hand.hqm.hqm_iqc_inspection_template_h_his.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_iqc_inspection_template_h_his.dto.IqcInspectionTemplateHHis;

public interface IqcInspectionTemplateHHisMapper extends Mapper<IqcInspectionTemplateHHis>{
	
	/**
	 * 查询历史数据中离审批时间最近的数据
	 * @author kai.li
	 * @param dto
	 * @return
	 */
	List<IqcInspectionTemplateHHis> selectbyheadIdTime(IqcInspectionTemplateHHis dto);
}