package com.hand.hqm.hqm_msa_stability_value.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_msa_stability_value.dto.MsaStabilityValue;

public interface MsaStabilityValueMapper extends Mapper<MsaStabilityValue>{
	/**
	 * 稳定数据性删除
	 * @param dto
	 */
	void deleteByMsaPlanId(MsaStabilityValue dto);
}