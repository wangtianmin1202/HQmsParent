package com.hand.hcm.hcm_item_b.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcm.hcm_item_b.dto.ItemTl;

public interface ItemTlMapper extends Mapper<ItemTl>{

	void updateByItemPlantId(ItemTl itu);

}