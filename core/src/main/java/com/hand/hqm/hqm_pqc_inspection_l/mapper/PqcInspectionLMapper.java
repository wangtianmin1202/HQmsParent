package com.hand.hqm.hqm_pqc_inspection_l.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_pqc_inspection_l.dto.PqcInspectionL;

public interface PqcInspectionLMapper extends Mapper<PqcInspectionL>{

	/**
	 * 基础查询
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
	List<PqcInspectionL> myselect(PqcInspectionL dto);
	

	List<PqcInspectionL> selectfornon(PqcInspectionL dto);

}