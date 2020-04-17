package com.hand.hqm.hqm_fqc_inspection_l.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_fqc_inspection_l.dto.FqcInspectionL;

public interface FqcInspectionLMapper extends Mapper<FqcInspectionL>{
	/**
	 * FQC检验单明细查询
	 * @param dto
	 * @return
	 */
	List<FqcInspectionL> query(FqcInspectionL dto);
	
	List<FqcInspectionL> queryfornon(FqcInspectionL dto);

	/**
	 * @description 当前检验单同物料 + 来源标识为“FQC.首次检验”+检验状态为“已完成”+检验时间倒排，往前找N条检验单行的该检验项的记录，包括当前这条检验项记录
	 * @author tianmin.wang
	 * @date 2020年2月19日 
	 * @param model
	 * @return
	 */
	List<FqcInspectionL> timeItemSelectN(FqcInspectionL model);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月31日 
	 * @param dto
	 * @return
	 */
	List<FqcInspectionL> selectAttribute(FqcInspectionL dto);
}