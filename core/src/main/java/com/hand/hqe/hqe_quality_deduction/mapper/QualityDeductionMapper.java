package com.hand.hqe.hqe_quality_deduction.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqe.hqe_quality_deduction.dto.QualityDeduction;

public interface QualityDeductionMapper extends Mapper<QualityDeduction>{
	
	/**
	 * 质量扣款单据录入界面 查询
	 * @param qualityDeduction 质量扣款单据
	 * @return
	 */
	List<QualityDeduction>	query(QualityDeduction qualityDeduction);
	/**
	 * 获取最大流水号
	 * @param qualityDeduction 质量扣款单据
	 * @return
	 */
	Integer queryMaxNum(QualityDeduction qualityDeduction);
}