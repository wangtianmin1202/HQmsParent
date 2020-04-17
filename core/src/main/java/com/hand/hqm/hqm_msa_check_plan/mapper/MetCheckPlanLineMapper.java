package com.hand.hqm.hqm_msa_check_plan.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_msa_check_plan.dto.MetCheckPlanLine;

public interface MetCheckPlanLineMapper extends Mapper<MetCheckPlanLine>{
	/**
	 * 校验历史查询
	 * @param metCheckPlanLine
	 * @return
	 */
	public List<MetCheckPlanLine> selectCheckPlanLine(MetCheckPlanLine metCheckPlanLine);
	/**
	 * 校验历史查询
	 * @param metCheckPlanLine
	 * @return
	 */
	List<MetCheckPlanLine> query(MetCheckPlanLine metCheckPlanLine);
}