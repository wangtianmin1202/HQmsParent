package com.hand.hqm.hqm_fqc_inspection_template_l.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_fqc_inspection_template_l.dto.FqcInspectionTemplateL;

public interface FqcInspectionTemplateLMapper extends Mapper<FqcInspectionTemplateL>{
	
	/**
	 * 
	 * @description 查询
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
	public List<FqcInspectionTemplateL> myselect(FqcInspectionTemplateL dto);
}