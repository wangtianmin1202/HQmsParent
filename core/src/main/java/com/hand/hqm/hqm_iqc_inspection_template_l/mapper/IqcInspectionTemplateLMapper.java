package com.hand.hqm.hqm_iqc_inspection_template_l.mapper;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_iqc_inspection_template_l.dto.IqcInspectionTemplateL;

public interface IqcInspectionTemplateLMapper extends Mapper<IqcInspectionTemplateL>{
	
	/**
	 * 基础查询
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
	public List<IqcInspectionTemplateL> myselect(IqcInspectionTemplateL dto);
	
	/**
	 * 依据主键的查询
	 * @description
	 * @author tianmin.wang
	 * @date 2020年2月20日 
	 * @param dto
	 * @return
	 */
	public List<IqcInspectionTemplateL> reSelect(IqcInspectionTemplateL dto);
}