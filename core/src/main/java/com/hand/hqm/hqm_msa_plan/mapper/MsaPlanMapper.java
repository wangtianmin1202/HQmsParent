package com.hand.hqm.hqm_msa_plan.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_msa_plan.dto.MsaPlan;

public interface MsaPlanMapper extends Mapper<MsaPlan>{

	/**
	 * MSA计划制定查询 
	 * @param msaPlan
	 * @return
	 */
	List<MsaPlan> query(MsaPlan msaPlan);
	/**
	 * 获取当天最大流水号
	 * @return
	 */
	Integer queryMaxNum();
}