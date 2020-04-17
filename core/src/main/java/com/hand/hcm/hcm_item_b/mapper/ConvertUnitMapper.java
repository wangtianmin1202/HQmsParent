package com.hand.hcm.hcm_item_b.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcm.hcm_item_b.dto.ConvertUnit;

public interface ConvertUnitMapper extends Mapper<ConvertUnit>{

	void deleteByItemPlantId(ConvertUnit del);
	void reInsert(ConvertUnit del);
}