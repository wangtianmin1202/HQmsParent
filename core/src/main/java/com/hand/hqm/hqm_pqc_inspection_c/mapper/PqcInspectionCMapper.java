package com.hand.hqm.hqm_pqc_inspection_c.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_pqc_inspection_c.dto.PqcInspectionC;
import com.hand.hqm.hqm_pqc_inspection_l.dto.PqcInspectionL;

public interface PqcInspectionCMapper extends Mapper<PqcInspectionC>{

	/**
	 * 基础查询
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
	List<PqcInspectionC> myselect(PqcInspectionC dto);
	
	
	
	List<PqcInspectionC> selectfornon(PqcInspectionC dto);
}