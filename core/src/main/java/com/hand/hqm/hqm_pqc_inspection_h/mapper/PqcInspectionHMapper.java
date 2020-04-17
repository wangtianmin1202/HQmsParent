package com.hand.hqm.hqm_pqc_inspection_h.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_pqc_inspection_h.dto.PqcInspectionH;
import com.hand.hqm.hqm_temporary_inspection.dto.TemporaryInspection;

public interface PqcInspectionHMapper extends Mapper<PqcInspectionH>{

	/**
	 * 基础查询
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
	List<PqcInspectionH> myselect(PqcInspectionH dto);

	
	/**
	 * 最大流水号
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param sr
	 * @return
	 */
	List<PqcInspectionH> selectMaxNumber(PqcInspectionH sr);
	
	/**
	 * 综合查询
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
	List<PqcInspectionH> qmsQuery(PqcInspectionH dto);

	/**
	 * 
	 * @description 根据物料组ID 找模板 临时检验项 
	 * @author tianmin.wang
	 * @date 2019年12月12日 
	 * @return
	 */
	List<PqcInspectionH> selectByCategoryId(TemporaryInspection dto);
	
}