package com.hand.hqm.hqm_iqc_inspection_template_h.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_inspection_group_h.dto.InspectionGroupH;
import com.hand.hqm.hqm_iqc_inspection_template_h.dto.IqcInspectionTemplateH;
import com.hand.hqm.hqm_temporary_inspection.dto.TemporaryInspection;

public interface IqcInspectionTemplateHMapper extends Mapper<IqcInspectionTemplateH>{
	
	/**
	 * 
	 * @description 基础查询
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
	List<IqcInspectionTemplateH> myselect(IqcInspectionTemplateH dto);
	
	/**
	 * 
	 * @description 复制功能的查询
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
	List<IqcInspectionTemplateH> selectforCopy(IqcInspectionTemplateH dto);
	
	/**
	 * 
	 * @description 添加临时检验单用到的查询 根据物料组查询
	 * @author tianmin.wang
	 * @date 2019年12月10日 
	 * @param dto
	 * @return
	 */
	List<IqcInspectionTemplateH> selectByCategoryId(TemporaryInspection dto);
}