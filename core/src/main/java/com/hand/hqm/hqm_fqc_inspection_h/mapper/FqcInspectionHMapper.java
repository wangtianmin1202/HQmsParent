package com.hand.hqm.hqm_fqc_inspection_h.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_fqc_inspection_h.dto.FqcInspectionH;
import com.hand.hqm.hqm_temporary_inspection.dto.TemporaryInspection;

public interface FqcInspectionHMapper extends Mapper<FqcInspectionH>{

	List<FqcInspectionH> selectMaxNumber(FqcInspectionH dto);
	List<FqcInspectionH> selectByNumber(FqcInspectionH dto);
	List<FqcInspectionH> qmsQuery(FqcInspectionH dto);
	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月12日 
	 * @param ti
	 * @return
	 */
	List<FqcInspectionH> selectByCategoryId(TemporaryInspection ti);
	
	List<FqcInspectionH> getLimitCount(FqcInspectionH dto);
}