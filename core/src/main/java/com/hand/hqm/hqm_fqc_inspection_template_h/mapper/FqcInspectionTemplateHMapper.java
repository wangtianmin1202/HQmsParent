package com.hand.hqm.hqm_fqc_inspection_template_h.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_fqc_inspection_template_h.dto.FqcInspectionTemplateH;

public interface FqcInspectionTemplateHMapper extends Mapper<FqcInspectionTemplateH>{
	/**
	 * 
	 * @description 基础查询
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
	List<FqcInspectionTemplateH> myselect(FqcInspectionTemplateH dto);
}