package com.hand.hqm.hqm_pqc_inspection_d.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_pqc_inspection_d.dto.PqcInspectionD;

public interface PqcInspectionDMapper extends Mapper<PqcInspectionD>{
	
	/**
	 * 
	 * @description 依据C表主键删除D表数据
	 * @author tianmin.wang
	 * @date 2019年12月12日 
	 * @param c
	 */
	void deleteByConnectId(com.hand.hqm.hqm_pqc_inspection_c.dto.PqcInspectionC c);



}