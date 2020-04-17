package com.hand.spc.ecr_main.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.ecr_main.dto.EcrItemResult;

public interface EcrItemResultMapper extends Mapper<EcrItemResult>{
	
	/**
	 *  修改审批结果
	 * @param dto 参数
	 */
	void updateState(EcrItemResult dto);
}