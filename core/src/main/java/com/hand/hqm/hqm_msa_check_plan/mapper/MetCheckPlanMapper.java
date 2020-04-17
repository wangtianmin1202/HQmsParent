package com.hand.hqm.hqm_msa_check_plan.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_msa_check_plan.dto.MetCheckPlan;

public interface MetCheckPlanMapper extends Mapper<MetCheckPlan>{
	/**
	 * 校验历史查询
	 * @param metCheckPlan
	 * @return
	 */
	public List<MetCheckPlan> selectCheckPlan(MetCheckPlan metCheckPlan);
	/**
	 * 获取当天最大流水号
	 * @param metCheckPlan
	 * @return
	 */
	Integer queryMaxNum();
}